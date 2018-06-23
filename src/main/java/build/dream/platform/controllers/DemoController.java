package build.dream.platform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/demo")
public class DemoController {
    @RequestMapping(value = "/demo")
    @ResponseBody
    public String demo() {
        List<byte[]> list = new ArrayList<byte[]>();
        boolean flag = true;
        while (flag) {
            list.add(new byte[1024 * 1024]);
        }
        return UUID.randomUUID().toString();
    }
}
