package com.miempresa.evaluacion_final.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode);
            model.addAttribute("error", error != null ? error.toString() : null);
            model.addAttribute("message", message != null ? message.toString() : null);

            if (statusCode == 400) {
                return "error/400";
            } else if (statusCode == 403) {
                return "error/403";
            } else if (statusCode == 404) {
                return "error/404";
            } else if (statusCode == 500) {
                return "error/500";
            }
        }
        return "error/500";
    }
}