package github.evertonbrunosds.notepad.view;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.evertonbrunosds.notepad.controller.UserprofileController;
import github.evertonbrunosds.notepad.model.request.SigninRequest;
import github.evertonbrunosds.notepad.model.request.SignupRequest;
import github.evertonbrunosds.notepad.model.response.SigninResponse;
import github.evertonbrunosds.notepad.model.response.SignupResponse;
import github.evertonbrunosds.notepad.model.shared.UserprofileShared;
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
        var userprofile = modelMapper.map(request, UserprofileShared.class);
        userprofile = controller.create(userprofile);
        return modelMapper.map(userprofile, SignupResponse.class);
    }

    @PostMapping
    @RequestMapping(value = Routes.SIGNIN)
    public SigninResponse signin(final @RequestBody SigninRequest request) {
        final var userprofileShared = controller.findByEmail(request.getEmail());
        final var token = controller.authenticate(content -> {
            content.setEmail(request.getEmail());
            content.setPassword(request.getPassword());
            content.setExpirationTime(EXPIRATION_TIME);
        });
        response.addHeader("Authorization", "Bearer ".concat(token));
        return modelMapper.map(userprofileShared, SigninResponse.class);
    }
    
}
