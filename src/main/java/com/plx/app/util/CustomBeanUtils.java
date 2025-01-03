package com.plx.app.util;

import org.springframework.context.ApplicationContext;

public class CustomBeanUtils {
    public static Object getBean(String beanId) {
         
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
 
        if( applicationContext == null ) {
            throw new NullPointerException("Spring의 ApplicationContext초기화 안됨");
        }
         
        /*
        String[] names = applicationContext.getBeanDefinitionNames();
        for (int i=0; i<names.length; i++) {
            System.out.println(names[i]);
        }
        */
        return applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> type) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		return applicationContext.getBean(type);
	}


    // public static Object getBean(Class<?> classType) {        
    //     ApplicationContext applicationContext = ApplicationContextProvider.getContext().getApplicationContext();
    //     return applicationContext.getBean(classType);    

    // }
}
