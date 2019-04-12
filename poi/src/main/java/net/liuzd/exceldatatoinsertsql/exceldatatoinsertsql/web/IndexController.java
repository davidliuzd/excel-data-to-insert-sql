package net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

    @RequestMapping("/")
    public String index() {
        log.debug("index...");
        return "index";
    }

}