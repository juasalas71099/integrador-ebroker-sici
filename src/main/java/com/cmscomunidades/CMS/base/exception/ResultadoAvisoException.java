package com.cmscomunidades.CMS.base.exception;


public class ResultadoAvisoException extends ResultadoException {
    private ResultadoException resultadoException;

    public ResultadoAvisoException(ResultadoException resultadoException) {
        this.resultadoException = resultadoException;
        super.setErrores(resultadoException.getErrores());
        super.setAvisos(resultadoException.getAvisos());
    }

    public ResultadoException getResultadoException() {
        return resultadoException;
    }

    public void setResultadoException(ResultadoException resultadoException) {
        this.resultadoException = resultadoException;
    }

}
