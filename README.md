# Neon Annotation

Neon là java preprocessor library. Neon sử dụng annotation để xử lí các thuộc tính của class và auto generate java class phục vụ serialize và deserialize.

## Usage

You can build from source to use this library.

## Basic example
Một class sử dụng Neon luôn cần implements Serializable.

Annotatate class bằng @Neon và khai báo thuộc tính.
```java

@Neon
public class Example implements Serializable {
    public int example1;
    public float example2;
}

```

Thêm 2 phương thức <b>bắt buộc</b> serialize và createFromSerialized theo cấu trúc như bên dưới để Serialize và Deserialize.

```java

@Neon
public class Example implements Serializable {
    
    //...property 
    
    @Override
    public void serialize(SerializedOutput serializedOutput) {
        Example__Neon.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromSerialized(SerializedInput input, DebugBuilder builder) {
            Example result = new Example();
            Example__Neon.createFromSerialized(result, input, builder);
            return result;
        }
    };
}

```
<b>Lưu ý:</b> Neon sẽ xử lí và tạo ra class có dạng \<Class\>__Neon gồm 2 phương thức:<br>
 
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
|NeonObject |Bao gồm các class đã được annotate bằng @Neon|
|Object       | Một Object bất kì chưa khai báo ở trên. Tuy nhiên phải tự tạo adapter để sử dụng. Xem thêm [Custom Adapter](#custom-adapter)

Ngoài kiểu Object bất kỳ ở trên, các kiểu còn lại đều có thể khai báo theo kiểu mảng [].

### Modifiers

Không được sử dụng private cho các thuộc tính.

Có thể sử dụng từ khóa transient  để bỏ qua việc serialization.

Có hỗ trợ inner static class.

## Quản lý version

Khi thêm một thuộc tính vào class, chúng ta cần đảm bảo rằng những dữ liệu từ version cũ vẫn có thể được revert. Neon hỗ trợ quản lý version để đảm bảo có thể sử dụng dữ liệu ở các version cũ hơn.

Khai báo: 
```java 
@Neon(version=1, compatibleSince=1)
public class Example implements Serializable {
   
   public int base;
   
   @Neon.Property(sinceVersion=1)
   public int additionData;
   
   //.. methods
}
```

Như ví dụ trên, để khai báo version của class, ta sử dụng @Neon(version=1), để biết một property xuất hiện từ version nào, ta sử dụng @Neon.Property(sinceVersion=1).<br>
Để ngưng hỗ trợ các version cũ, ta thêm @Neon(compatibleSince=1). Khi đó các binary có version < 1 sẽ báo lỗi nếu sử dụng.<br>
Mặc định version = 0, sinceVersion=0. Ngoài ra, sinceVersion không được lớn hơn version.<br>
<b>Quan trọng:</b> Việc quản lý version chỉ áp dụng khi thêm thuộc tính vào class. Việc xóa tên thuộc tính có thể dẫn đến những dữ liệu của version cũ hoạt động sai.

<b>Note: </b>Sửa tên thuộc tính không ảnh hưởng đến quá trình serialize.

## Serialize Parent

Neon hỗ trợ việc serialize và deserialize một class, mà class được dc extends từ parent. Khi đó, Neon sẽ hỗ trợ serialize từ parent, và parent cũng phải là Neon Object. Có thể thiết lập như sau:
```java
@Neon(inheritanceSupported = true)
public class NeonChild extends NeonParent implements Serializable {
    public String daddyName;
    
    //...

}    
```

Mặc định inheritanceSupported là true.

## DEBUG
Sử dụng class SerializableHelper để lấy dữ liệu debug.

```java
class Example {
    public void sample() {
        SerializableHelper<NeonSample> helper = new SerializableHelper();
        Map.Entry<NeonSample, String> result = helper.deserialize(serializedInput, NeonSample.CREATOR, true);
        // Print structure of object
        Log.d(TAG, result.getValue());
    }
}    
```

## Sử dụng các annotations khác.

Sử dụng @NonNull hoặc @NotNull cho một thuộc tính không được phép null. Khi đó zarcel sẽ cho phép đọc ghi dữ liệu mà không cần kiểm tra.

Sử dụng @Deprecated thông báo về một thuộc tính không nên sử dụng nữa. Tuy nhiên, Neon vẫn sẽ đọc và ghi thuộc tính này.

## Migrator annotation

Sử dụng để thay đổi giá trị của object sau khi để deserialized. Ví dụ: Thay đổi giá trị mặc định của một thuộc tính nếu như version < MIN_VERSION.

```java
 @Neon(version = 4)
 @Migrator(ColorMigrator.class)
 public class ZColor implements Serializable {
     int color = Color.TRANSPARENT;
 }
 ```
 
 ```java
  public class ColorMigrator implements NeonMigrator<ZColor> {
    @Override
    void migrate(ZColor object, int fromVersion, int toVersion) {
        if (fromVersion <= 3) {
            // Sample code
            object.color = Color.TRANSPARENT;
        }
    }    
  }
  ```
  
  <b>Note:</b> Neon chỉ gọi Migrator khi class này <b>implement</b> NeonMigrator<? extends Serializable>

## Custom Adapter

Phần này sẽ hướng dẫn sử dụng một adapter tùy biến, được sử dụng để serialize và deserialize một kiểu dữ liệu tùy chỉnh, không thể sử dụng Neon Object.

#### Bước một: Tạo Adapter

Một adapter <b>bắt buộc</b> implements <i>NeonAdapter</i>. Tham số chính là class cần serialize.
 ```java
 public class AnimalAdapter implements NeonAdapter<NeonAnimal> {
     @Override
     public void serialize(@NonNull NeonAnimal object, SerializedOutput writer) {
         // Do something
         /*
            Example: 
            writer.writeInt32(object.a);
          */
     }
 
     @Override
     public NeonAnimal createFromSerialized(SerializedInput reader) {
         // Do something
          /*
             Example: 
             return NeonAnimal.createObject(reader.readInt32());
           */
     }
 }
 ```
 
  ```java
  
  public class CarAdapter implements NeonAdapter<Car[]> {
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

 @Neon
 public class World implements Serializable {
    
    @Neon.Custom(adapter = AnimalAdapter.class)
    NeonAnimal cat;
    
    @Neon.Custom(adapter = CarAdapter.class)
    Car[] cars;
    //...Methods
 }
 
```

## Một số adapter có sẵn

```gradle
implementation 'com.zing.zalo.neon:zarcel-utils:<version>'
```

### NeonDateAdapter

Adapter dùng cho Serialization kiểu java.lang.Date

Sử dụng: 

```java

 @Neon
 public class Human implements Serializable {
    
    @Neon.Custom(adapter = DateNeonAdapter.class)
    Date birthday;
    
    //...Methods
 }
 
```

### PolymorphismNeonAdapter

Adapter dùng để serialize các lớp con của một lớp cha, khi những lớp con này được sử dụng 'đa hình' và được khai báo như một lớp cha. 

#### Bước một: Tạo một adapter extends PolymorphismNeonAdapter

Sau khi tạo xong, override phương thức như bên dưới: 

```java

public class VehicleAdapter extends PolymorphismNeonAdapter<NeonVehicle> {

    @Override
    protected void onRegisterChildClasses() {
        try {
            registryAdd(NeonVehicle.CAR, NeonCar.class);
            registryAdd(NeonVehicle.BIKE, NeonBike.class);
        } catch (NeonDuplicateException e) {
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

    // Parent: NeonVehicle
    // Child: NeonBike, NeonCar
    // largestVehicle is NeonBike or NeonCar.
    @Neon.Custom(adapter = VehicleAdapter.class)
    public NeonVehicle largestVehicle;
    
    //...Methods
}

```
