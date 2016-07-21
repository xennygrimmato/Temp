from rest_framework import serializers

from .models import *

class CategorySerializer(serializers.ModelSerializer):
    #name = serializers.CharField(max_length=255)
    class Meta:
        model = Category
        fields = ('id', 'name', )

class ProductSerializer(serializers.ModelSerializer):
    code = serializers.CharField(max_length=200)
    description = serializers.CharField(max_length=1000)
    price = serializers.IntegerField()
    #id = serializers.IntegerField()
    category_id = serializers.IntegerField(source='get_category_id', read_only=True, required=False)
    category = serializers.CharField(max_length=1000,write_only=True, required=False)
    #category_name = serializers.CharField(max_length=255, source='category.name', read_only=True)
    class Meta:
        model = Product
        fields = ('id', 'code', 'description', 'price', 'category_id', 'category',)

    def create(self, validated_data):
        category = validated_data['category']
        del validated_data['category']

        # create category
        #
        '''
        try:
            category_obj = Category.objects.filter(name=category).first()
        except:
            category_obj = Category(name=category)
            category_obj.save()
        '''
        category_obj,created=Category.objects.get_or_create(name=category)
        # create product
        product_obj = Product(**validated_data)
        product_obj.deleted=0
        product_obj.save()
        # add entry to categoryproduct
        category_product_obj,created = CategoryProduct.objects.get_or_create(category=category_obj, product=product_obj)
        #print self.__dict__
        return product_obj

    def update(self, instance, validated_data):
        if not self.partial:
            # PUT
            code = validated_data['code'] if 'code' in validated_data else ''
            instance.code = code
            description = validated_data['description'] if 'description' in validated_data else ''
            instance.description = description
            price = validated_data['price'] if 'price' in validated_data else '0'
            instance.price = int(price)
            category = validated_data['category'] if 'category' in validated_data else ''
            instance.save()
            # if category doesnt exist create it
            try:
                category_obj = Category.objects.get(name=category)
            except:
                category_obj = Category(name=category, description="")
                category_obj.save()
            # assign that category id to that product id
            try:
                category_product_obj = CategoryProduct.objects.get(category=category_obj, product=instance)
            except:
                category_product_obj = CategoryProduct(category=category_obj, product=instance)
                category_product_obj.save()
            return instance

        else:
            # PATCH
            instance.code = validated_data['code'] if 'code' in validated_data else instance.code
            instance.description = validated_data['description'] if 'description' in validated_data else instance.description
            instance.price = int(price) if 'price' in validated_data else int(instance.price)
            cat_name = validated_data['category'] if 'category' in validated_data else ''
            instance.save()
            try:
                category_obj = Category.objects.get(name=cat_name)
            except:
                category_obj = Category(name=cat_name, description="")
                category_obj.save()
            # save (category, product)
            try:
                category_product_obj = CategoryProduct.objects.get(category=category_obj, product=instance)
            except:
                category_product_obj = CategoryProduct(category=category_obj, product=instance)
                category_product_obj.save()
            return instance


class CategoryProductSerializer(serializers.ModelSerializer):

    # Refering to name of Category using the source parameter
    # Here, Category is a foreign key for CategoryProduct's category ID
    # Hence, we can use "category.name" as:
    #   1. category -> name of attribute of CategoryProduct
    #   2. foreign key relation enables use of "." operator for
    #      traversing to the associated attribute
    product_name = serializers.CharField(max_length=255, source='product.name', read_only = True)
    product_code = serializers.CharField(max_length=20, source='category.code', read_only = True)
    product_price = serializers.IntegerField(source = 'product.price', read_only = True)
    product_id = serializers.IntegerField(source='product.id', read_only = True)
    category_name = serializers.CharField(max_length=255, source='category.name', read_only = True)
    category_id = serializers.IntegerField(source='category.id', read_only = True)

    class Meta:
        model = CategoryProduct
        fields = ('category_name', 'product_code', 'product_id', 'product_name', 'product_price', 'category_id', )

class OrderSerializer(serializers.ModelSerializer):

    id = serializers.IntegerField(source='oid', read_only=True)
    username = serializers.CharField(max_length=100, source='uid.company_name', required=False)
    address = serializers.CharField(max_length=512, source='uid.addr_line1', required=False)
    status = serializers.CharField(max_length=45, required=True)

    class Meta:
        model = Orders
        fields = ('id', 'username', 'address', 'status', )

    def update(self, instance, validated_data):
        username = validated_data['uid']['company_name'] if 'company_name' in validated_data['uid'] else None
        address = validated_data['uid']['addr_line1'] if 'addr_line1' in validated_data['uid'] else None
        if not self.partial:
            #PUT
            #pk, username, address, status
            #with open('/Users/vaibhavtulsyan/as6/a6/tmp.txt', 'w') as f:
            #    f.write(str(validated_data))

            try:
                # update user details
                user = User.objects.get(company_name=username)
                if not address:
                    # address was not provided in the input
                    # do nothing, do not update address
                    user.addr_line1 = ""
                else:
                    user.addr_line1 = address
            except:
                # create new user
                if address:
                    user = User(company_name=username, addr_line1=address)
                else:
                    user = User(company_name=username, addr_line1="")
            user.save()

            status_str = validated_data['status'] if 'status' in validated_data else 'Created'
            instance.uid = user
            instance.status = status_str # update status
            instance.save()
            return instance

        else:
            #PATCH
            # user get or create logic
            if 'company_name' in validated_data['uid'] \
                             and validated_data['uid']['company_name'] != ""\
                             and validated_data['uid']['company_name'] != None:
                username = validated_data['uid']['company_name']
                user = User.objects.get_or_create(company_name=username)[0]
            else:
                username = instance.uid.company_name
                user = User.objects.get_or_create(company_name=username)[0]

            if 'addr_line1' not in validated_data['uid']:
                # do nothing
                pass
            else:
                user.addr_line1 = validated_data['uid']['addr_line1']
                user.save()
            instance.uid = user
            instance.status = validated_data['status'] if 'status' in validated_data else instance.status
            instance.save() # note, either all changes happen or none. this is transactional
            return instance


class OrderLineItemSerializer(serializers.ModelSerializer):

    #id = serializers.IntegerField(source='id', read_only=True)
    price = serializers.IntegerField(source='selling_cost')
    product_id = serializers.IntegerField(source='product.id')
    order_id = serializers.IntegerField(source='order.oid', read_only=True)

    class Meta:
        model = OrderProduct2
        fields = ('id', 'product_id', 'order_id', 'price',)

class OrderSummarySerializer(serializers.ModelSerializer):
    #id = serializers.IntegerField(source='id', read_only=True)
    price = serializers.IntegerField(source='selling_cost')
    product_id = serializers.IntegerField(source='product.id')
    order_id = serializers.IntegerField(source='order.oid', read_only=True)

    class Meta:
        model = OrderProduct2
        fields = ('id', 'product_id', 'order_id', 'price',)
