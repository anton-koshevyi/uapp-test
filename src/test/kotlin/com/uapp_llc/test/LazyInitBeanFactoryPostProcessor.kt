package com.uapp_llc.test

import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory

class LazyInitBeanFactoryPostProcessor : BeanFactoryPostProcessor {

  override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    for (beanName in beanFactory.beanDefinitionNames) {
      beanFactory.getBeanDefinition(beanName).isLazyInit = true
    }
  }

}
