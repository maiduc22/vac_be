package hiep.com.springboot.controllers;

import hiep.com.springboot.payload.response.UserActivityInfo;
import hiep.com.springboot.services.SessionService;
import hiep.com.springboot.common.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Session resource")
@Validated
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/", "https://hrm-frontend-two.vercel.app/"})
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Get all session")
    @GetMapping("")
//    @PreAuthorize("hasPermission(null, 'user:view')")
    public Response<List<UserActivityInfo>> getAll() {
        return Response.of(sessionService.getAllSessions());
    }
}
