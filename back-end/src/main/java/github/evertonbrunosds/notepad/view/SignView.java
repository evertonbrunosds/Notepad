package github.evertonbrunosds.notepad.view;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.evertonbrunosds.notepad.controller.UserprofileController;
import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.model.request.SigninRequest;
import github.evertonbrunosds.notepad.model.request.SignupRequest;
import github.evertonbrunosds.notepad.model.response.SigninResponse;
import github.evertonbrunosds.notepad.model.response.SignupResponse;
import github.evertonbrunosds.notepad.util.Routes;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SignView {

    private final static Integer EXPIRATION_TIME = 864000000;

    private final UserprofileController controller;

    private final HttpServletResponse response;

    private final ModelMapper modelMapper;

    @PostMapping
    @RequestMapping(value = Routes.SIGNUP)
    public SignupResponse signup(final @RequestBody SignupRequest request) {
        final var userprofile = modelMapper.map(request, UserprofileEntity.class);
        return modelMapper.map(controller.create(userprofile), SignupResponse.class);
    }

    @PostMapping
    @RequestMapping(value = Routes.SIGNIN)
    public SigninResponse signin(final @RequestBody SigninRequest request) {
        final var jwtAuthentication = controller.authenticate(content -> {
            content.setEmail(request.getEmail());
            content.setPassword(request.getPassword());
            content.setExpirationTime(EXPIRATION_TIME);
        });
        response.addHeader("Authorization", "Bearer ".concat(jwtAuthentication.token()));
        response.addHeader("X-Expiration-Date-Time", jwtAuthentication.timeLimit().toString());
        return modelMapper.map(jwtAuthentication.userprofile(), SigninResponse.class);
    }
    
}
