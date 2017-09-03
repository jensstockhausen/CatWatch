package de.famst.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Controller
public class CatPicsController
{
    private @Value("${cat.watch.catpicsfolder}")
    String catPicsFolder;

    @RequestMapping(value = "/cat/{channel:[\\d]+}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> catPic(
        @PathVariable("channel") int channel) throws IOException
    {
        String picFileName = String.format("%s/cat_%02d.png", catPicsFolder, channel);

        InputStream in = null;

        if (Paths.get(picFileName).toFile().exists())
        {
            in = new FileInputStream(Paths.get(picFileName).toFile());
        } else
        {
            String fileName = "";

            switch(channel)
            {
                case 2: fileName = String.format("/catpics/%s.png", "turkish-van"); break;
                case 3: fileName = String.format("/catpics/%s.png", "chartreux"); break;
                case 4: fileName = String.format("/catpics/%s.png", "turkish-angora"); break;
                case 5: fileName = String.format("/catpics/%s.png", "japanese-bobtail"); break;
                default: fileName = String.format("/catpics/%s.png", "singapura"); break;
            }

            in =  ClassLoader.class.getResourceAsStream(fileName);
        }

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(new InputStreamResource(in));
    }


}
