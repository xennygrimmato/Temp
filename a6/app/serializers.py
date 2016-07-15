from rest_framework import serializers

from .models import *

class CategorySerializer(serializers.ModelSerializer):
    #name = serializers.CharField(max_length=255)
    class Meta:
        model = Category
        fields = ('id', 'name')

class ProductSerializer(serializers.ModelSerializer):
    code = serializers.CharField(max_length=20, required=True)
    description = serializers.CharField(max_length=1000, required=True)
    price = serializers.DecimalField(max_digits=15, decimal_places=5, required=True)
    #id = serializers.IntegerField()
    category_id = serializers.IntegerField(source='get_category_id', read_only=True)
    category = serializers.CharField(max_length=255,write_only=True, required=True)
    class Meta:
        model = Product
        fields = ('id', 'code', 'description', 'price', 'category_id', 'category', )

    def create(self, validated_data):
        code = validated_data['code']
        description = validated_data['description']
        price = validated_data['price']
        category = validated_data['category']

        # create category
        try:
            category_obj = Category.objects.filter(name=category).first()
        except:
            category_obj = Category(name=category)
            category_obj.save()
        # create product
        product_obj = Product(code=code, description=description, price=round(float(price),4), deleted=0)
        product_obj.save()
        # add entry to categoryproduct
        category_product_obj = CategoryProduct.objects.get_or_create(category=category_obj, product=product_obj)
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
            instance.price = round(float(price),4)
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
            instance.price = round(float(validated_data['price']),4) if 'price' in validated_data else round(float(instance.price),4)
            instance.save()
            try:
                category_obj = Category.objects.get(name=category)
            except:
                category_obj = Category(name=validated_data['category'], description="")
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
    category_name = serializers.CharField(max_length=255, source='category.foo')

    class Meta:
        model = CategoryProduct
        fields = ('category_name',)
