# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from __future__ import unicode_literals

from django.db import models

class Category(models.Model):
    name = models.CharField(max_length=255)
    description = models.CharField(max_length=1000, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'category'


class CategoryProduct(models.Model):
    category_id = models.IntegerField()
    product_id = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'category_product'


class Feedback(models.Model):
    text = models.CharField(max_length=255)
    timestamp = models.DateTimeField(blank=True, null=True)
    user = models.ForeignKey('User', models.DO_NOTHING, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'feedback'


class OrderProduct2(models.Model):
    order = models.ForeignKey('Orders', models.DO_NOTHING)
    product = models.ForeignKey('Product', models.DO_NOTHING)
    buying_cost = models.DecimalField(max_digits=10, decimal_places=4, blank=True, null=True)
    selling_cost = models.DecimalField(max_digits=10, decimal_places=4, blank=True, null=True)
    quantity = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'order_product2'
        unique_together = (('order', 'product'),)


class Orders(models.Model):
    oid = models.AutoField(primary_key=True)
    uid = models.ForeignKey('User', models.DO_NOTHING, db_column='uid', blank=True, null=True)
    amount = models.DecimalField(max_digits=10, decimal_places=4, blank=True, null=True)
    timestamp = models.DateTimeField(blank=True, null=True)
    status = models.CharField(max_length=45, blank=True, null=True)
    id = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'orders'


class Product(models.Model):
    name = models.CharField(max_length=255)
    remaining = models.IntegerField(blank=True, null=True)
    code = models.CharField(max_length=20, blank=True, null=True)
    description = models.CharField(max_length=1000, blank=True, null=True)
    deleted = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'product'


class QueueData(models.Model):
    timestamp = models.DateTimeField(blank=True, null=True)
    url = models.CharField(max_length=1024, blank=True, null=True)
    parameters = models.CharField(max_length=1024, blank=True, null=True)
    response_code = models.CharField(max_length=255, blank=True, null=True)
    ip = models.CharField(max_length=255, blank=True, null=True)
    method = models.CharField(max_length=255, blank=True, null=True)
    duration = models.CharField(max_length=35, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'queue_data'


class User(models.Model):
    email = models.CharField(max_length=255, blank=True, null=True)
    first_name = models.CharField(max_length=100, blank=True, null=True)
    last_name = models.CharField(max_length=100, blank=True, null=True)
    company_name = models.CharField(max_length=100, blank=True, null=True)
    phone = models.CharField(max_length=20, blank=True, null=True)
    addr_line1 = models.CharField(max_length=512, blank=True, null=True)
    addr_line2 = models.CharField(max_length=512, blank=True, null=True)
    city = models.CharField(max_length=45, blank=True, null=True)
    state = models.CharField(max_length=45, blank=True, null=True)
    postal_code = models.CharField(max_length=45, blank=True, null=True)
    country = models.CharField(max_length=45, blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'user'
