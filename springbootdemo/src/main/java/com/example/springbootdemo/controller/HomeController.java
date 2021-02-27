package com.example.springbootdemo.controller;

import com.example.springbootdemo.entities.Categories;
import com.example.springbootdemo.entities.Countries;
import com.example.springbootdemo.entities.Users;
import com.example.springbootdemo.entities.shopItems;
import com.example.springbootdemo.services.ItemService;
import com.example.springbootdemo.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @GetMapping(value = "/")
    public String index(Model model){
        ArrayList<shopItems>items=(ArrayList<shopItems>) itemService.getAllItems();
        ArrayList<Countries>countries= (ArrayList<Countries>) itemService.getAllCountries();
        model.addAttribute("tovary",items);
        model.addAttribute("countries",countries);
        model.addAttribute("current_user",getUserData());
        return "index";
    }

    @GetMapping(value = "/about")
    public String about(Model model){
        model.addAttribute("current_user",getUserData());
        return "about";
    }

    @PostMapping(value = "/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
     public String addItem(@RequestParam(name="item_name", defaultValue = "No item") String name,
                           @RequestParam(name="item_price", defaultValue = "0") int price,
                           @RequestParam(name = "item_amount", defaultValue = "0") int amount,
                           @RequestParam(name = "country_id", defaultValue = "0L") Long id){
        Countries countries=itemService.getCountry(id);
        if (countries!=null){
            shopItems item=new shopItems();
            item.setName(name);
            item.setPrice(price);
            item.setAmount(amount);
            item.setCountry(countries);
            if (item!=null){
                itemService.addItem(item);
            }
        }

        return "redirect:/";
    }

    @GetMapping(value = "/details/{idshka}")
    public String details(Model model,@PathVariable(name = "idshka") Long id){
        shopItems item=itemService.getItem(id);
        List<Countries> countries=itemService.getAllCountries();
        List<Categories>categories=itemService.getAllCategories();
        model.addAttribute("current_user",getUserData());
        model.addAttribute("item",item);
        model.addAttribute("countries",countries);
        model.addAttribute("categories",categories);
        return "details";
    }

    @GetMapping(value = "/edititem/{idshka}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String editItem(Model model,@PathVariable(name = "idshka") Long id){
        shopItems item=itemService.getItem(id);
        List<Countries> countries=itemService.getAllCountries();
        List<Categories>categories=itemService.getAllCategories();
        categories.removeAll(item.getCategories());
        model.addAttribute("current_user",getUserData());
        model.addAttribute("item",item);
        model.addAttribute("countries",countries);
        model.addAttribute("categories",categories);
        return "editItem";
    }


    @PostMapping(value = "/saveItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "id",defaultValue = "0") Long id,
                           @RequestParam(name="item_name", defaultValue = "No item") String name,
                           @RequestParam(name="item_price", defaultValue = "0") int price,
                           @RequestParam(name = "item_amount", defaultValue = "0") int amount,
                           @RequestParam(name="country_id",defaultValue = "0") Long country_id){
           shopItems item=itemService.getItem(id);
           if (item!=null){

               Countries country=itemService.getCountry(country_id);
               if (country!=null){
                   item.setName(name);
                   item.setAmount(amount);
                   item.setPrice(price);
                   item.setCountry(country);
                   itemService.saveItem(item);
               }

           }
        return "redirect:/edititem/"+id;
    }
    @PostMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name = "item_id") Long id){
        shopItems item=itemService.getItem(id);
        if (item!=null){
            itemService.deleteItem(item);
        }
        return "redirect:/";
    }

    @PostMapping(value = "/assigncategory")
    public String assignCategory(@RequestParam(name="category_id") Long category_id,
                                 @RequestParam(name = "item_id") Long item_id) {
        Categories cat=itemService.getCategory(category_id);
        if (cat!=null){
            shopItems item=itemService.getItem(item_id);
            if (item!=null){
                List<Categories>categories=item.getCategories();
                if (categories==null){
                    categories=new ArrayList<>();
                }
                    categories.add(cat);
                    item.setCategories(categories);
                    itemService.saveItem(item);
                    return "redirect:/edititem/"+item_id+"#categoriesDiv";
            }
        }
        return "redirect:/";
    }
    @PostMapping(value = "/unassign")
    public String unassign(@RequestParam(name="category_id") Long category_id,
                                 @RequestParam(name = "item_id") Long item_id) {
        Categories cat=itemService.getCategory(category_id);
        if (cat!=null){
            shopItems item=itemService.getItem(item_id);
            if (item!=null){
                List<Categories>categories=item.getCategories();
                if (categories==null){
                    categories=new ArrayList<>();
                }
                categories.remove(cat);
                item.setCategories(categories);
                itemService.saveItem(item);
                return "redirect:/edititem/"+item_id+"#categoriesDiv";
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("current_user",getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("current_user",getUserData());
        return "login";
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("current_user",getUserData());
        return "register";
    }

    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name="userEmail") String email,
                             @RequestParam(name = "userFullname") String fullname,
                             @RequestParam(name = "userPassword") String password,
                             @RequestParam(name = "re_userPassword") String Repassword)
    {
        if (password.equals(Repassword)){
            Users user=new Users();
            user.setFullname(fullname);
            user.setPassword(password);
            user.setEmail(email);
             if (userService.createUsers(user)!=null){
                 return "redirect:/register?success";
             }
        }
               return "redirect:/register?error";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("current_user",getUserData());
        return "profile";
    }

    @GetMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addItem(Model model){
        model.addAttribute("current_user",getUserData());
        ArrayList<shopItems>items=(ArrayList<shopItems>) itemService.getAllItems();
        ArrayList<Countries>countries= (ArrayList<Countries>) itemService.getAllCountries();
        model.addAttribute("tovary",items);
        model.addAttribute("countries",countries);
        return "addItem";
    }

    private Users getUserData(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser=(User)authentication.getPrincipal();
            Users myUser=userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(
            @RequestParam(name = "user_avatar")MultipartFile file
            ){
        if (file.getContentType().equals("image/jpeg")|| file.getContentType().equals("image/png")){
            try {
                Users currentUser=getUserData();
                String picName= DigestUtils.sha1Hex("Avatar_"+currentUser.getId()+"_!Picture");
                byte[]bytes=file.getBytes();
                Path path= Paths.get(uploadPath+picName+".jpg");
                Files.write(path,bytes);
                currentUser.setUser_avatar(picName);
                userService.saveUser(currentUser);
                return "redirect:/profile?success";
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/viewphoto/{url}",produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[]viewProfilePhoto(
            @PathVariable(name = "url") String url
    )throws IOException
    {
         String pictureUrl=viewPath+defaultPicture;
         if (url!=null && !url.equals("null")){
             pictureUrl=viewPath+url+".jpg";
         }

        InputStream in;
         try{
             ClassPathResource pathResource=new ClassPathResource(pictureUrl);
             in=pathResource.getInputStream();
         }catch (Exception ex){
             ClassPathResource pathResource=new ClassPathResource(viewPath+defaultPicture);
             in=pathResource.getInputStream();
             ex.printStackTrace();
         }

         return IOUtils.toByteArray(in);
    }

}
