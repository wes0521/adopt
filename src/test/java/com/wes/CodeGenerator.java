package com.wes;

/**
 * *
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //            佛祖保佑       永不宕机      永无BUG                  //
 * ////////////////////////////////////////////////////////////////////
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * @Author LengXiaoStudio
 * @ClassName CodeGenerator
 * @date 2020.12.18 15:06
 */

public class CodeGenerator {
    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {


        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //1.全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir"); //获取当前项目路径
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("wes");        //设置作者																//自己修改**********
        gc.setOpen(false);          //生成后是否打开资源管理器
        gc.setFileOverride(true);   //重新生成文件的时候是否会覆盖
        gc.setIdType(IdType.AUTO);  //设置id的策略
        gc.setServiceName("%sService"); //接口 比如 IUserService 去掉  I

        gc.setDateType(DateType.ONLY_DATE); //设置日期类型
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        //2. 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql:///mypet?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");				//自己修改**********
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");																							//自己修改**********
        dsc.setPassword("111");																				//自己修改**********
        dsc.setDbType(DbType.MYSQL); //设置数据库类型
        mpg.setDataSource(dsc);

        // 3.包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.wes");																						//自己修改**********
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        mpg.setPackageInfo(pc);

        // 4.策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(",")); //映射的表名

        strategy.setNaming(NamingStrategy.underline_to_camel); //数据库映射到实体类的策略。驼峰命名
        strategy.setTablePrefix("t_"); //不生成表的前缀																		//自己修改**********
        strategy.setEntityLombokModel(true); //自动添加lombok注解
        strategy.setRestControllerStyle(true);

        //逻辑字段
        strategy.setLogicDeleteFieldName("is_deleted");// 逻辑删除字段名  不生成is_							//自己修改**********
        strategy.setEntityBooleanColumnRemoveIsPrefix(true); // 逻辑删除字段 是否删除 前缀 比如上一行的 is_

        //自动填充 生成时间&更新时间
        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);									//自己修改**********
        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);					//自己修改**********
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        strategy.setTableFillList(tableFills);

        //乐观锁的列
        strategy.setVersionFieldName("version");										//自己修改**********（一般不用）

        //RestFUL 风格

        strategy.setRestControllerStyle(true);

        //url 驼峰命名 转化为_
        strategy.setControllerMappingHyphenStyle(true);

        mpg.setStrategy(strategy);
        //执行
        mpg.execute();
    }

}

