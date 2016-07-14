from django.conf.urls import url
from django.contrib import admin

from . import views

urlpatterns = [
    url(r'^daily-sale', views.func)
]
