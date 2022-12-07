#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=2.7.5 \
--build=gradle \
--java-version=17 \
--packaging=jar \
--name=budget-type-service \
--package-name=al.codepie.microservices.core.budgettype \
--groupId=al.codepie.microservices.core.budgettype \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
budget-type-service

spring init \
--boot-version=2.7.5 \
--build=gradle \
--java-version=17 \
--packaging=jar \
--name=budget-item-service \
--package-name=al.codepie.microservices.core.budgetitem \
--groupId=al.codepie.microservices.core.budgetitem \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
budget-item-service

spring init \
--boot-version=2.7.5 \
--build=gradle \
--java-version=17 \
--packaging=jar \
--name=budget-expense-service \
--package-name=al.codepie.microservices.core.budgetexpense \
--groupId=al.codepie.microservices.core.budgetexpense \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
budget-expense-service

spring init \
--boot-version=2.7.5 \
--build=gradle \
--java-version=17 \
--packaging=jar \
--name=budget-aggregator-service \
--package-name=al.codepie.microservices.aggregator.budget\
--groupId=al.codepie.microservices.aggregator.budget \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
budget-aggregator-service

cd ..

