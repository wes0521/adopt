package com.wes.adopt.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.adopt.entity.Pet;
import com.wes.adopt.entity.Type;
import com.wes.adopt.entity.User;
import com.wes.adopt.mapper.PetMapper;
import com.wes.adopt.mapper.TypeMapper;
import com.wes.adopt.mapper.UsersMapper;
import com.wes.adopt.utils.IdUtils;
import com.wes.adopt.utils.PageModel;
import com.wes.adopt.utils.RedisUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wes
 * @since 2020-12-18
 */
@Controller
@RequestMapping("/adopt/pet")
public class PetController {

    @Resource
    private PetMapper petMapper;

    @Resource
    private TypeMapper typeMapper;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/findAllPets")
    public String findPets(@RequestParam(defaultValue = "1") Integer pageIndex,
                           @RequestParam(defaultValue = "3") Integer pageSize,
                           String type, String petName, Model model) {
        System.out.println("要查询的宠物名；" + petName);
        PageModel pageModel = new PageModel();
        pageModel.setPageIndex(pageIndex);
        pageModel.setPageSize(pageSize);
        IPage<Pet> petPage = new Page<>(pageIndex, pageSize);//参数一是当前页，参数二是每页个数
        String ts = redisTemplate.opsForValue().get("types");
        List<Type> types = new ArrayList<>();
        if(ts==null || "".equals(ts)){
            System.out.println("第一次查，redis没有数据");
            types = typeMapper.selectList(null);
            ts = JSON.toJSONString(types);
            redisTemplate.opsForValue().set("types",ts,60, TimeUnit.SECONDS);
        }else{
            System.out.println("redis缓存有types");
            types = JSON.parseArray(ts,Type.class);
        }
        //所有宠物类型打印
        types.forEach(System.out::println);
        if ("".equals(petName) || null == petName) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("is_deleted", 0);
            int recordCount = petMapper.selectCount(wrapper);
            pageModel.setRecordCount(recordCount);
            petPage = petMapper.selectPage(petPage, wrapper);
        } else {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("is_deleted", 0);
            wrapper.like("petName", petName);
            int recordCount = petMapper.selectCount(wrapper);
            pageModel.setRecordCount(recordCount);
            petPage = petMapper.selectPage(petPage, wrapper);
        }
        List<Pet> pets = petPage.getRecords();
        //查询出宠物类型表。遍历宠物为其赋值对应的类型名称，（原是数字id）
        for (Pet pet : pets) {
            for (Type type1 : types) {
                if (String.valueOf(type1.getId()).equals(pet.getPetType())) {
                    pet.setPetType(type1.getPetType());
                    break;
                }
            }
        }
        System.out.println("pets------>>" + pets.size());
        pets.forEach(System.out::println);//打印动物列表
        model.addAttribute("pets", pets);
        model.addAttribute("pageModel", pageModel);
        if (petName != null) {
            model.addAttribute("petName", petName);
        } else {
            model.addAttribute("petName", "");
        }
        if (type == null || "".equals(type)) {
            System.out.println("type为空说明是前台的查询动物");
            return "/html/adopt";
        }
        System.out.println("type不为空说明是后台的查询动物type为：" + type);
        return "/admin/admin-animal";
    }

    /*
     * 删除单个动物通过id
     * */
    @RequestMapping("/delAnimalById")
    @ResponseBody
    public String delAnimalById(String id) {
        System.out.println("要删除的动物id" + id);
        //删除之前查询redis缓存是否有此动物数据，有则删除
        String animalById = redisTemplate.opsForValue().get("animal"+id);
        if(animalById != null){
            redisTemplate.delete("animal"+id);
            System.out.println("redis缓存中有id为"+id+"的动物得数据,进行删除缓存");
        }
        QueryWrapper q = new QueryWrapper();
        q.eq("id", id);
        Pet pet = petMapper.selectOne(q);
        pet.setIs_deleted(1);
        int result = petMapper.update(pet, q);
        if (result > 0) {
            System.out.println("动物删除成功！");
        } else {
            System.out.println("动物删除失败！");
            return "fail";
        }
        return "success";
    }

    /*
     * 通过id批量删除动物
     * */
    @RequestMapping("/delManyAnimalById")
    @ResponseBody
    public String delManyAnimalById(String ids) {
        System.out.println("ids--->" + ids);
        boolean flag = true;
        if (ids != null && !ids.equals("")) {
            String[] idss = ids.split(",");
            for (String id : idss) {
                System.out.println("要删除的id" + id);
                String animalById = redisTemplate.opsForValue().get("animal"+id);
                if(animalById != null){
                    redisTemplate.delete("animal"+id);
                    System.out.println("redis缓存中有id为"+id+"的动物得数据,进行删除缓存");
                }
                if (id != null && !"".equals(id)) {
                    QueryWrapper qw = new QueryWrapper();
                    qw.eq("id", id);
                    Pet pet = petMapper.selectOne(qw);
                    pet.setIs_deleted(1);
                    int result = petMapper.update(pet, qw);
                    if (result == 0) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            System.out.println("动物删除成功！");
            return "success";
        } else {
            System.out.println("动物删除失败！");
            return "fail";
        }
    }

    /*
     * 添加动物
     * */
    @RequestMapping("/addPet")
    @ResponseBody
    public String addPet(Pet pet) {
        System.out.println("要新增的动物信息：" + pet);
        int result = petMapper.insert(pet);
        if (result != 0) {
            return "success";
        }
        return "fail";
    }
    /*
     * 上传图片
     * */
    @RequestMapping("/upload/headImg")
    @ResponseBody
    public Map<String,Object> headImg(@RequestParam(value="file",required=false) MultipartFile upload, HttpServletRequest request, Model model) throws IOException {
        System.out.println("图片开始上传！！！");
        String pic = "";
        int code=1;
        Map<String,Object> res = new HashMap<>();
        res.put("code",code);
        res.put("imgPath",pic);
        if(!upload.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("/images/animals");
            System.out.println("wenjainmim:"+upload.getOriginalFilename());
            //保存图片到项目磁盘路径（换成自己的项目磁盘路径）
            String path1 = "E:\\JavaEE\\bishe\\adopt\\src\\main\\resources\\static\\images\\animals";
            System.out.println("保存路径为："+path1);
            //文件上传路径不存在，创建路径
            File file = new File(path);
            //判断上传路径是否存在，不存在则创建
            if(!file.exists()){
                file.mkdirs();
            }
            //获取文件名
            String fileName= IdUtils.getUUID()+"_"+upload.getOriginalFilename();
            System.out.println("图片名称"+fileName);

            //将MultipartFile转换为File，保存到服务器对应地址
            upload.transferTo(new File(path,fileName));
//            upload.transferTo(new File(path1,fileName));
            //文件拷贝到项目路径
            FileUtils.copyFile(new File(path,fileName),new File(path1,fileName));
            //return fileName;
            pic = "/images/animals/"+fileName;
            System.out.println("图片上传成功！！！");
            code = 0;
            res.put("imgPath",pic);
            res.put("code",code);
        }
//        model.addAttribute("pic",pic);
        System.out.println("回传的res："+res);
        return res;
    }


    /*
     * 修改单个动物之前查询动物信息==通过id
     * */
    @RequestMapping("/findAnimalInfoById")
    public String findAnimalInfoById(String id, String type, Model model, HttpSession session) {
        System.out.println("要修改&查询的动物id-->" + id);
        String animalById = redisTemplate.opsForValue().get("animal"+id);
        Pet pet = null;
        //查找动物
        if(animalById == null || "".equals(animalById)){
            System.out.println("首次查找，redis缓存没有id为"+id+"的动物信息");
            QueryWrapper<Pet> q = new QueryWrapper();
            q.eq("id", id);
            pet = petMapper.selectOne(q);
            redisTemplate.opsForValue().set("animal"+id, JSON.toJSONString(pet),60, TimeUnit.SECONDS);
        }else{
            pet = JSON.parseObject(animalById,Pet.class);
            System.out.println("缓存中有id为"+id+"的动物信息，直接获取");
        }
        //查询动物所有类型
        String ts = redisTemplate.opsForValue().get("types");
        List<Type> types = null;
        if(ts == null || "".equals(ts)){
            System.out.println("第一次查，redis没有types动物的所有类型数据");
            types = typeMapper.selectList(null);
            redisTemplate.opsForValue().set("types", JSON.toJSONString(types),60, TimeUnit.SECONDS);
        }else{
            System.out.println("redis缓存有types");
            types = JSON.parseArray(ts,Type.class);
        }

        for (Type type2 : types) {
            if (String.valueOf(type2.getId()).equals(pet.getPetType())) {
                pet.setPetType(type2.getPetType());
                break;
            }
        }

        //针对修改过数据库之后，当前用户的领养状态会及时更正
        User user = (User) session.getAttribute("user");
        if(user != null) {
            User user1 = usersMapper.selectById(user.getId());
            session.setAttribute("user", user1);
            System.out.println("当前用户的信息" + user1);
        }

        model.addAttribute("animalById", pet);
        System.out.println("type-type="+type);
        if(type == null || "".equals(type)){
            System.out.println("要查询的动物信息animal-->" + pet);
            return "/html/animalInfo";
        }else {
            System.out.println("要修改的动物信息animal->" + pet);
            return "/admin/member-editAnimal";
        }

    }

    /*
     * 修改单个动物信息==通过id
     * */
    @RequestMapping("/modifyAnimalById")
    @ResponseBody
    public String modifyAnimalById(Pet pet) {
        System.out.println("修改之后的pet------>" + pet);
        //数据修改之后若有缓存就需要删除
        String animalById = redisTemplate.opsForValue().get("animal"+pet.getId());
        if(animalById != null){
            redisTemplate.delete("animal"+pet.getId());
        }
        QueryWrapper<Pet> qw = new QueryWrapper();
        qw.eq("id", pet.getId());
        int result = petMapper.update(pet, qw);
        System.out.println("result->" + result);
        if (result == 0) {
            System.out.println("修改动物信息失败");
            return "fail";
        } else {
            System.out.println("修改动物信息成功");
            return "success";
        }

    }


}

