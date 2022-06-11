# wave_3d 🌊
#### Visualization of the water surface/waves in 3D based on 2D Perlin Noise.

## A few words :raised_hands:
This visualization is essentially **a slightly modified [terrain visualization](https://github.com/ZERDICORP/terrain_3d)**.  
Therefore, there is not much to say here. The only thing I would like to note is how, having perlin noise, you can achieve the **effect of waves**..  

But there is **nothing complicated** here.  
In fact, we need to **shift our noise** along, for example, the X axis at each moment of time.  

*We will get the following result:*  
<details>
  <summary>spoiler</summary>

  ![2022-06-12 00-31-23](https://user-images.githubusercontent.com/56264511/173205588-3cd02322-3a1a-4213-8d42-dfc7f92d74d6.gif)
</details>

***Doesn't really look like waves, does it?***  
***Maybe..***  

So, to fix this, we need to **combine** different **noise values**!

That is, in addition to the noise value for the **current element of the two-dimensional matrix**, we must take some more values from **other points of the matrix**.  

Then just **add** them up, **dividing** by the **total number of noise values**.  

*Here is some pseudocode:*
```java
n1 = noise(x + offset, y);
n2 = noise(x, y + offset);
n3 = noise((matrixWidth - x) + offset, y);
n4 = noise(x, (matrixHeight - y) + offset);

noiseMap[y][x] = (n1 + n2 + n3 + n4) * 0.4;
```

## How can I try it? 🏃
> I am using IntellijIDEA, btw
#### 1. Just open the project in the IDE and run the following file
```
src/main/java/just/curiosity/wave_3d/Main.java
```

## GIF :point_right::point_left:

![2022-06-12 00-49-28](https://user-images.githubusercontent.com/56264511/173206043-10c92a38-0d20-4c8c-89d2-48232531fd04.gif)
