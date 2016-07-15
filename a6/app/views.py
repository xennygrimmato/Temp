from django.shortcuts import render
from django.http import JsonResponse
from models import *
import datetime
# Create your views here.
from django.db.models import *
from django.views.decorators.csrf import csrf_exempt

from rest_framework import viewsets
from serializers import *

@csrf_exempt
def func(request):
    if 'startDate' in request.GET:
        startDate = request.GET['startDate']
    else:
        startDate = "01/01/1999"

    if 'endDate' in request.GET:
        endDate = request.GET['endDate']
    else:
        endDate = "31/12/2020"

    startDate = "-".join(x for x in startDate.split("/"))
    endDate = "-".join(x for x in endDate.split("/"))

    start_date = datetime.datetime.strptime(startDate, "%m-%d-%Y").date()
    end_date = datetime.datetime.strptime(endDate, "%m-%d-%Y").date()


    #ret = models.Orders.objects.filter(amount__gte=5000)
    #ret = models.OrderProduct2.objects.filter(order__amount__gte=6000)
    ret = models.OrderProduct2.objects.filter(order__timestamp__gte=start_date, order__timestamp__lte=end_date).extra({'date':"date(timestamp)"}).values('date').order_by('-date').annotate(orders=Count('id'), qty=Sum('quantity'), buy_price=Sum(F('quantity') * F('buying_cost'), output_field=DecimalField(max_digits=10, decimal_places=4)), sale_price=Sum(F('quantity') * F('selling_cost'), output_field=DecimalField(max_digits=10, decimal_places=4)), profit=Sum(F('quantity') * F('selling_cost'), output_field=DecimalField(max_digits=10, decimal_places=4))-Sum(F('quantity') * F('buying_cost'), output_field=DecimalField(max_digits=10, decimal_places=4)))

    lst = [obj for obj in ret]

    for obj in lst:
        tmp = obj["date"].strftime("%Y-%m-%d").split("-")
        obj["date"] = tmp[1] + "/" + tmp[2] + "/" + tmp[0]
        obj["buy_price"] = float(obj["buy_price"])
        obj["sale_price"] = float(obj["sale_price"])
        obj["profit"] = float(obj["profit"])

    return JsonResponse({
                         "data": lst
                        })

class CategoryViewSet(viewsets.ModelViewSet):
    queryset = Category.objects.all()
    serializer_class = CategorySerializer

class ProductViewSet(viewsets.ModelViewSet):
    queryset = Product.objects.filter(deleted=0)
    serializer_class = ProductSerializer

class CategoryProductViewSet(viewsets.ModelViewSet):
    queryset = CategoryProduct.objects.all()
    serializer_class = CategoryProductSerializer
