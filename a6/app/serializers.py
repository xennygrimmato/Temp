from rest_framework import serializers

from .models import *

class CategorySerializer(serializers.ModelSerializer):
    #name = serializers.CharField(max_length=255)
    class Meta:
        model = Category
        fields = ('id', 'name')

class ProductSerializer(serializers.ModelSerializer):
    #code = serializers.CharField(max_length=20)
    #description = serializers.CharField(max_length=1000)
    #price = serializers.DecimalField(max_digits=10, decimal_places=4)
    category_id = serializers.IntegerField(source='get_category_id', read_only=True)
    category_name = serializers.CharField(max_length=255,write_only=True)
    class Meta:
        model = Product
        fields = ('id', 'code', 'description', 'price', 'category_id', 'category_name', )

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
