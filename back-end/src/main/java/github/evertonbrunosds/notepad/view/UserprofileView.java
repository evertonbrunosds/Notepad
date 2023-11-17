package github.evertonbrunosds.notepad.view;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.evertonbrunosds.notepad.controller.UserprofileController;
import github.evertonbrunosds.notepad.model.entity.UserprofileEntity;
import github.evertonbrunosds.notepad.model.request.UserprofileRequest;
import github.evertonbrunosds.notepad.model.response.UserprofileResponse;
import github.evertonbrunosds.notepad.security.model.Author;
import github.evertonbrunosds.notepad.util.Routes;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = Routes.USERPROFILE)
public class UserprofileView {

    private final UserprofileController controller;

    private final Author author;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> create() {
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @GetMapping(value = "/email={email}")
    public UserprofileResponse findByEmail(final @PathVariable String email) {
        final var shared = controller.findByEmail(email);
        return modelMapper.map(shared, UserprofileResponse.class);
    }

    @GetMapping(value = "/id_userprofile_pk={idUserprofilePk}")
    public UserprofileResponse findByIdUserprofilePk(final @PathVariable UUID idUserprofilePk) {
        final var shared = controller.findByIdUserprofilePk(idUserprofilePk);
        return modelMapper.map(shared, UserprofileResponse.class);
    }

    @PutMapping
    public UserprofileResponse update(final @RequestBody UserprofileRequest request) {
        var shared = modelMapper.map(request, UserprofileEntity.class);
        shared.setIdUserprofilePk(author.getUserprofile().getIdUserprofilePk());
        shared.setCreated(author.getUserprofile().getCreated());
        shared = controller.update(shared);
        return modelMapper.map(shared, UserprofileResponse.class);
    }

    @DeleteMapping
    public UserprofileResponse delete() {
        final var shared = author.getUserprofile();
        final var response = modelMapper.map(shared, UserprofileResponse.class);
        controller.deleteByIdUserprofilePk(response.getIdUserprofilePk());
        return response;
    }

}
