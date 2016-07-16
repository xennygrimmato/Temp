"""a6 URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url, include
from django.contrib import admin
from rest_framework import routers
import app.urls
from app import views

router = routers.DefaultRouter()
router.register(r'products', views.ProductViewSet)
router.register(r'categories', views.CategoryViewSet)
router.register(r'categoryproducts', views.CategoryProductViewSet)
router.register(r'orders', views.OrderViewSet)
router.register(r'orders/(?P<order_id>[0-9]+)/orderlineitem', views.OrderLineViewSet)

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^api/reports/', include(app.urls)),
    url(r'^api/', include(router.urls)),
    url(r'^api/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^api/docs/', include('rest_framework_swagger.urls')),
    url(r'^api/orders/summary', views.order_summary_view),
    url(r'^api/products/summary', views.product_summary_view),
    url(r'^api/health', views.health),
]
