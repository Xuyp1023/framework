/**
 * <pre>
 * Copyright:		Copyright(C) 2011-2012, ketayao.com
 * Filename:		com.dcfund.system.exception.ExistedException.java
 * Class:			ExistedException
 * Date:			2012-8-13
 * Author:			<a href="mailto:ketayao@gmail.com">ketayao</a>
 * Version          1.1.0
 * Description:		
 *
 * </pre>
 **/

package com.betterjr.common.exception;

/**
 * 
 * @author zhoucy
  */

public class ExistedException extends ServiceException {

    /** 描述 */
    private static final long serialVersionUID = -598071452360556064L;

    public ExistedException() {
        super();
    }

    public ExistedException(String message) {
        super(message);
    }

    public ExistedException(Throwable cause) {
        super(cause);
    }

    public ExistedException(String message, Throwable cause) {
        super(message, cause);
    }
}
