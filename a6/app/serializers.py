from rest_framework import serializers

from .models import *

class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ('code', 'description')
