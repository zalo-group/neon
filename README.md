# Zarcel Annotation

Zarcel là java preprocessor library. Zarcel sử dụng annotation để xử lí các thuộc tính của class và auto generate java class phục vụ serialize và deserialize.

## Usage

<pre>
implementation 'com.zing.zalo:zarcel-annotations:0.4.0'
annotationProcessor 'com.zing.zalo:zarcel-processor:0.4.0'
</pre>

hoặc

Import from [source code](https://zalogit2.zing.vn/zinstant/zarceler/tags)

## Basic example
Một class sử dụng Zarcel luôn cần implements Serializable.

Annotatate class bằng @Zarcel và khai báo thuộc tính.
```java

@Zarcel
public class Example implements Serializable {
    public int example1;
    public float example2;
}

```

Thêm 2 phương thức <b>bắt buộc</b> serialize và createFromSerialized theo cấu trúc như bên dưới để Serialize và Deserialize.

```java

@Zarcel
public class Example implements Serializable {
    
    //...property 
    
    @Override
    public void serialize(SerializedOutput serializedOutput) {
        Example__Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromSerialized(SerializedInput input) {
            Example result = new Example();
            Example__Zarcel.createFromSerialized(result, input);
            return result;
        }
    };
}

```
<b>Lưu ý:</b> Zarcel sẽ xử lí và tạo ra class có dạng \<Class\>__Zarcel gồm 2 phương thức:<br>
 
 void serialize(\<Class\>, SerializedInput);<br>
 void deserialize(<Class>, SerializedOutput);<br>
 
 Các phương thức này có thể gọi đến 2 phương thức bắt buộc của class, hoặc gọi đến 2 phương thức của các class __Zacel khác. 

### Các kiểu dữ liệu được phép sử dụng

|Kiểu dữ liệu | Ghi Chú          | 
|------------ | ---------------- |
|boolean      |                  |
|int          |                  | 
|long         |                  | 
|float        |                  | 
|double       |                  | 
|String       |                  |
|ZarcelObject |Bao gồm các class đã được annotate bằng @Zarcel|
|Object       | Một Object bất kì chưa khai báo ở trên. Tuy nhiên phải tự tạo adapter để sử dụng. Xem thêm [Custom Adapter](#custom-adapter)

Ngoài kiểu Object bất kỳ ở trên, các kiểu còn lại đều có thể khai báo theo kiểu mảng [].

### Modifiers

Không được sử dụng private cho các thuộc tính.

Có thể sử dụng từ khóa transient  để bỏ qua việc serialization.

Có hỗ trợ inner static class.

## Quản lý version

Khi thêm một thuộc tính vào class, chúng ta cần đảm bảo rằng những dữ liệu từ version cũ vẫn có thể được revert. Zarcel hỗ trợ quản lý version để đảm bảo có thể sử dụng dữ liệu ở các version cũ hơn.

Khai báo: 
```java 
@Zarcel(version=1, compatibleSince=1)
public class Example implements Serializable {
   
   public int base;
   
   @Zarcel.Property(sinceVersion=1)
   public int additionData;
   
   //.. methods
}
```

Như ví dụ trên, để khai báo version của class, ta sử dụng @Zarcel(version=1), để biết một property xuất hiện từ version nào, ta sử dụng @Zarcel.Property(sinceVersion=1).<br>
Để ngưng hỗ trợ các version cũ, ta thêm @Zarcel(compatibleSince=1). Khi đó các binary có version < 1 sẽ báo lỗi nếu sử dụng.<br>
Mặc định version = 0, sinceVersion=0. Ngoài ra, sinceVersion không được lớn hơn version.<br>
<b>Quan trọng:</b> Việc quản lý version chỉ áp dụng khi thêm thuộc tính vào class. Việc xóa tên thuộc tính có thể dẫn đến những dữ liệu của version cũ hoạt động sai.

<b>Note: </b>Sửa tên thuộc tính không ảnh hưởng đến quá trình serialize.

## Serialize Parent

Zarcel hỗ trợ việc serialize và deserialize một class, mà class được dc extends từ parent. Khi đó, Zarcel sẽ hỗ trợ serialize từ parent, và parent cũng phải là Zarcel Object. Có thể thiết lập như sau: 
```java
@Zarcel(inheritanceSupported = true)
public class ZarcelChild extends ZarcelParent implements Serializable {
    public String daddyName;
    
    //...

}    
```

Mặc định inheritanceSupported là true.

## Sử dụng các annotations khác.

Sử dụng @NonNull hoặc @NotNull cho một thuộc tính không được phép null. Khi đó zarcel sẽ cho phép đọc ghi dữ liệu mà không cần kiểm tra.

Sử dụng @Deprecated thông báo về một thuộc tính không nên sử dụng nữa. Tuy nhiên, Zarcel vẫn sẽ đọc và ghi thuộc tính này.

## Migrator annotation

Sử dụng để thay đổi giá trị của object sau khi để deserialized. Ví dụ: Thay đổi giá trị mặc định của một thuộc tính nếu như version < MIN_VERSION.

```java
 @Zarcel(version = 4)
 @Migrator(ColorMigrator.class)
 public class ZColor implements Serializable {
     int color = Color.TRANSPARENT;
 }
 ```
 
 ```java
  public class ColorMigrator implements ZarcelMigrator<ZColor> {
    @Override
    void migrate(ZColor object, int fromVersion, int toVersion) {
        if (fromVersion <= 3) {
            // Sample code
            object.color = Color.TRANSPARENT;
        }
    }    
  }
  ```
  
  <b>Note:</b> Zarcel chỉ gọi Migrator khi class này <b>implement</b> ZarcelMigrator<? extends Serializable>

## Custom Adapter

Phần này sẽ hướng dẫn sử dụng một adapter tùy biến, được sử dụng để serialize và deserialize một kiểu dữ liệu tùy chỉnh, không thể sử dụng Zarcel Object.

#### Bước một: Tạo Adapter

Một adapter <b>bắt buộc</b> implements <i>ZarcelAdapter</i>. Tham số chính là class cần serialize.
 ```java
 public class AnimalAdapter implements ZarcelAdapter<ZarcelAnimal> {
     @Override
     public void serialize(@NonNull ZarcelAnimal object, SerializedOutput writer) {
         // Do something
         /*
            Example: 
            writer.writeInt32(object.a);
          */
     }
 
     @Override
     public ZarcelAnimal createFromSerialized(SerializedInput reader) {
         // Do something
          /*
             Example: 
             return ZarcelAnimal.createObject(reader.readInt32());
           */
     }
 }
 ```
 
  ```java
  
  public class CarAdapter implements ZarcelAdapter<Car[]> {
      @Override
      public void serialize(@NonNull Car[] object, SerializedOutput writer) {
          // Do something
      }
  
      @Override
      public Car[] createFromSerialized(SerializedInput reader) {
          // Do something
      }
  }
  
  ```

#### Bước 2: Sử dụng Adapter

Để sử dụng adapter, ta thực hiện như sau: 

```java

 @Zarcel
 public class World implements Serializable {
    
    @Zarcel.Custom(adapter = AnimalAdapter.class)
    ZarcelAnimal cat;
    
    @Zarcel.Custom(adapter = CarAdapter.class)
    Car[] cars;
    //...Methods
 }
 
```

## Một số adapter có sẵn

<pre> 
implementation 'com.zing.zalo:zarcel-utils:0.4.0'
</pre>

### ZarcelDateAdapter 

Adapter dùng cho Serialization kiểu java.lang.Date

Sử dụng: 

```java

 @Zarcel
 public class Human implements Serializable {
    
    @Zarcel.Custom(adapter = DateZarcelAdapter.class)
    Date birthday;
    
    //...Methods
 }
 
```

### PolymorphismZarcelAdapter

Adapter dùng để serialize các lớp con của một lớp cha, khi những lớp con này được sử dụng 'đa hình' và được khai báo như một lớp cha. 

#### Bước một: Tạo một adapter extends PolymorphismZarcelAdapter

Sau khi tạo xong, override phương thức như bên dưới: 

```java

public class VehicleAdapter extends PolymorphismZarcelAdapter<ZarcelVehicle> {

    @Override
    protected void onRegisterChildClasses() {
        try {
            registryAdd(ZarcelVehicle.CAR, ZarcelCar.class);
            registryAdd(ZarcelVehicle.BIKE, ZarcelBike.class);
        } catch (ZarcelDuplicateException e) {
            e.printStackTrace();
        }
    }
}

```

Tại phương thức onRegisterChildClasses, đăng ký toàn bộ subclass kế thừa từ parent class. <br>
Các phương thức được hỗ trợ: 

|Method                           | Ghi Chú          | 
|-------------------------------- | ----------------   |
|registryAdd(int type, Class)     | Thêm một class mới | 
|registryUpdate(int type, Class)  | Cập nhật class hiện tại | 
|registryRemove(int type)         | Xóa class khỏi danh sách |
|isRegistered(int type)           | Kiểm tra một class đã có trong danh sách chưa thông qua type| 

#### Bước hai: Sử dụng adapter

```java

public class Street implements Serializable {

    // Parent: ZarcelVehicle
    // Child: ZarcelBike, ZarcelCar
    // largestVehicle is ZarcelBike or ZarcelCar. 
    @Zarcel.Custom(adapter = VehicleAdapter.class)
    public ZarcelVehicle largestVehicle;
    
    //...Methods
}

```