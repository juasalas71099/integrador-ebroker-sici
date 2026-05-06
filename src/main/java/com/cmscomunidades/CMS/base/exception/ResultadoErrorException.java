package com.cmscomunidades.CMS.base.exception;


public class ResultadoErrorException extends ResultadoException {

    private ResultadoException resultadoException;

    public ResultadoErrorException(ResultadoException resultadoException) {
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
