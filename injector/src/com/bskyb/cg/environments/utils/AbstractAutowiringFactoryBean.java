/*
Copyright 2012 BSkyB Ltd.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.bskyb.cg.environments.utils;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractAutowiringFactoryBean<T> extends
AbstractFactoryBean<T> implements ApplicationContextAware{

private ApplicationContext applicationContext;

@Override
public void setApplicationContext(
    final ApplicationContext applicationContext){
    this.applicationContext = applicationContext;
}

@Override
protected final T createInstance() throws Exception{
    final T instance = getInstance();
    if(instance != null){
        applicationContext
          .getAutowireCapableBeanFactory()
          .autowireBean(instance);
    }
    return instance;
}

protected abstract T getInstance();

}

