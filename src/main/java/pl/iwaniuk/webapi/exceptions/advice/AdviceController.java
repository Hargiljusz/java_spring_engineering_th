package pl.iwaniuk.webapi.exceptions.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.iwaniuk.webapi.exceptions.*;
import pl.iwaniuk.webapi.models.ErrorMessage;


@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(UserWithEmailExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage userExistsHandler(UserWithEmailExistException exception){
        return  new ErrorMessage("","This email is already taken");
    }

    @ExceptionHandler({
            KindNotFoundException.class,
            GroupNotFoundException.class,
            CourseNotFoundException.class,
            PostNotFoundException.class,
    UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage userExistsHandler(RuntimeException exception){
        return new ErrorMessage(exception.getClass().getSimpleName(),"Not Found");
    }


}
