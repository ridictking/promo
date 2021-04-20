package com.ng.emts.christmasOffer.api;



import com.ng.emts.christmasOffer.exceptions.BESApiClientException;
import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.exceptions.CBSApiClientException;
import com.ng.emts.christmasOffer.exceptions.NotFoundException;
import com.ng.emts.christmasOffer.model.base.CompleteLog;
import com.ng.emts.christmasOffer.model.base.Response;
import com.ng.emts.christmasOffer.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
public class ChristmasOfferApiErrorAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleBadRequestException(BadRequestException e){
        return createAPIResponse(e,"400");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response handleNotFoundException(NotFoundException e){
        return createAPIResponse(e,"404");
    }
    
    @ExceptionHandler(CBSApiClientException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleCbsClientException(CBSApiClientException e){
        return createAPIResponse(e,"400");
    }

    @ExceptionHandler(BESApiClientException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleBesClientException(BESApiClientException e){
        return createAPIResponse(e,"400");
    }

    private Response createAPIResponse(Exception e, String code){
        e.printStackTrace();
        CompleteLog log = LogUtil.getExceptionMessageWithGUID(e);
        logger.error(log.getUuid() + "\n" + log.getMessage());
        Response response = new Response(code, e.getMessage());
        response.setLogId(log.getUuid());

        return response;
    }
}
