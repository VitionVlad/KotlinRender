# KotlinSoftwareRender
Software Kotlin Render
It use swing to create window, and to draw inside it, here is also included a obj file reader, with simple meshes and in low res, it even works fast
![Снимок экрана от 2022-10-15 15-50-28](https://user-images.githubusercontent.com/48290199/195987349-cf312e38-f6da-4d83-bb55-0e3bc6b019f0.png)
because of rendering using swing polygons(in my case, triangles), you can make them wire, or solid
![Снимок экрана от 2022-10-15 15-55-34](https://user-images.githubusercontent.com/48290199/195987516-ab1ff7ad-e220-4858-8cca-1bd8b2356e58.png)
Some performance notes: i added backface culling, this means that only triangles in clockwise order will render, in other words:
![Снимок экрана от 2022-10-16 20-55-57](https://user-images.githubusercontent.com/48290199/196050569-69dd4464-852a-4811-a422-21ae238206ea.png)
in this triangle i render only 6 triangles, instead of 12, because only 6 are in clockwise, for me it means that only geomtry faced to me will render
![Снимок экрана от 2022-10-16 20-56-08](https://user-images.githubusercontent.com/48290199/196050627-e8235efe-95a5-4868-b054-59d5709b9afb.png)
in this image i entered a little bit inside th cube, as you can see, nothing renders, because everything inside is counter-clockwise.  
For player this mean that only geometry that he can see will be rendered
