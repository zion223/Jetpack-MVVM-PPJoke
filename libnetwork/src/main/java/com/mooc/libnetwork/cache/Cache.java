package com.mooc.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

@Entity(tableName = "cache" //表名
        // , indices = {@Index(value = "key", unique = false)}//本表索引，用于大量数据的查询优化，unique有时候需要保证数据表的某个或者某些字段只有唯一的记录，可以通过设置@Index注解的unique属性实现。以下实例代码实现了避免有两条记录包含一样的key值。

        //  , inheritSuperIndices = false//如果 该值为true,那么父类中标记的indices{}索引也会算作该表的索引

        //  , primaryKeys = {"key"}//主键,一些策略逻辑会用到，比如插入一条数据时如果已存在,则更新否则算新的插入,那么怎么判断 ,数据库中是否已存在该条数据呢？就判断提供的主键,在表中是否已存在

        //  , foreignKeys = {
        //外键,一般用于多表数据查询.可以配置多个外键
        //ForeignKey用来设置关联表数据更新时所进行的操作，比如可以在@ForeignKey注解中设置onDelete=CASCADE，这样当Cache表中某个对应记录被删除时，ForeignTable表的所有相关记录也会被删除掉。
        //对于@Insert(OnConflict=REPLACE)注解，SQLite是进行REMOVE和REPLACE操作，而不是UPDATE操作，这个可能影响到foreign key的约束。


        //value:关联查询的表的Java.class,这里给定ForeignTable.class
        //parentColumns:与之关联表ForeignTable表中的列名
        //childColumns:本表的列的名称,必须要和parentColumns个数一致。这两个可以理解为根据cache表中的那个字段去比对ForeignTable表中的那个字段，认为是有关联关系的数据。
        //onDelete:关联表中某条记录被delete或update时,本表应该怎么做：
        //                                       NO_ACTION:什么也不做,
        //                                       RESTRICT:本表跟parentColumns有关系的数据会立刻删除或更新,但不允许一对多的关系,
        //                                       SET_NULL:本表所跟parentColumns有关系的数据被设置为null值，
        //                                       SET_DEFAULT:本表所有跟parentColumns有关系的数据被设置为默认值,也是null值
        //                                       CASCADE:本表所有跟parentColumns有关系的数据一同被删除或更新
        //onUpdate:本表中某条记录被更新时,与之关联的表应该怎么做
        //deferred:本表某条记录变更时，与之关联表的数据变更是否要立即执行,还是等待本表事务处理完再来处理关联表。默认是同时处理。
//        @ForeignKey(value = ForeignTable.class,
//                parentColumns = "foreign_key",
//                childColumns = "key",
//                onDelete = 1,
//                onUpdate = 1,
//                deferred = false)}
        //本表中 那些字段 不需要 映射到表中
        // , ignoredColumns = {"data"}

)
public class Cache implements Serializable {
    //PrimaryKey 必须要有,且不为空,autoGenerate 主键的值是否由Room自动生成,默认false
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String key;

    //@ColumnInfo(name = "_data"),指定该字段在表中的列的名字
    public byte[] data;

    //@Embedded 对象嵌套,ForeignTable对象中所有字段 也都会被映射到cache表中,
    //同时也支持ForeignTable 内部还有嵌套对象
    //public ForeignTable foreignTable;

}

//public class ForeignTable implements Serializable {
//    @PrimaryKey
//    @NonNull
//    public String foreign_key;
//
//    //@ColumnInfo(name = "_data")
//    public byte[] foreign_data;
//}
