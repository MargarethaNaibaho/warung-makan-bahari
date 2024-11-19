package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.MenuImage;
import com.enigmacamp.springbootwmbreview.repository.MenuImageRepository;
import com.enigmacamp.springbootwmbreview.service.MenuImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class MenuImageServiceImpl implements MenuImageService {
    private final MenuImageRepository menuImageRepository;

    private final Path directoryPath = Paths.get("/home/enigma/Documents/pelajaran/week 8/asset/image/");

    @Override
    public MenuImage createFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File isn't found"); //kita pake BAD_REQUEST karna isi request yg diterima itu jelek (di sini dalam artian ga ditemukan)

        try{
            //lokasi penyimpanan file di direktori
            //paths ini dipake untuk nyari apakah path yg dikasih itu ada ga
            //path nya ini nanti diarahkan ke path server saat di deploy, bukan di lokal lagi
            //aku pake createDirectories dibandingkan createDirectory karena kalo pake createDirectory dan pathdirectory nya itu udah exist, dia akan throw error. kalo pake createDirectories, barula dia bisa
            Files.createDirectories(directoryPath);

            //ini alternatif lain untuk kode di atas
//            if(!Files.exists(directoryPath)){
//                Files.createDirectory(directoryPath);
//            }

            //penamaan file
            //System.current
            String filename = String.format("%d, %s", System.currentTimeMillis(), multipartFile.getOriginalFilename());

            //lokasi file beserta path directory
            //.resolve() ini dipake untuk gabungkan directoryPath dan filename nya. jadi nanti langsung ngehit gambarnya dengan path yg dibentuk dari kode di bawah
            Path filePath = directoryPath.resolve(filename);

            //penimpanan
            //standardcopyoptimeini maksudnya adalah StandardCopyOption.REPLACE_EXISTING adalah salah satu opsi yang digunakan saat menyalin atau memindahkan
            // file di Java. Jika opsi ini digunakan, maka file yang dituju akan diganti jika sudah ada file dengan nama yang sama di lokasi tujuan.
            //kode dibawah bertujuan untuk menyalin data gambar yang dihasilkan oleh multipartFile.getInputStream() ke lokasi yang ditentukan yaitu filePath
            //StandardCopyOption.REPLACE_EXISTING ini digunakan kalo ada file dengan nama yang sama di direktor tujuan, maka file tersebut akan diganti dengan file yang baru diupload
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            //logic untuk simpan di db
            MenuImage menuImage = MenuImage.builder()
                    .name(filename)
                    .contentType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .path(filePath.toString())
                    .build();

            return menuImageRepository.saveAndFlush(menuImage);

        } catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Resource findByPath(String path) {
        try{
            Path filePath = Paths.get(path);

            //knapa ini harus diubah ke uri terlebih dahulu sebelum dijadikan URL?
            //karna kelas UrlResource terima parameter bertipe uri
            return new UrlResource(filePath.toUri());
        } catch(MalformedURLException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public void deleteFile(String id) {

    }
}
