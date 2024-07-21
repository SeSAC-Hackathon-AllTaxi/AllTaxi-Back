package com.sesac.alltaxi.controller;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private RequestService requestService;

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/driver")
    public Driver createDriver(@RequestBody Driver driver) {
        return driverService.saveDriver(driver);
    }

    @PostMapping("/request")
    public Request createRequest(@RequestBody Request request) {
        // 음성 -> 텍스트 변환 및 생성형 AI 호출 로직 추가
        return requestService.saveRequest(request);
    }

    @GetMapping("/request/{id}")
    public Request getRequest(@PathVariable Long id) {
        return requestService.getRequestById(id).orElse(null);
    }
}
