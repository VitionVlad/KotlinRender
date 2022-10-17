# KotlinRender
Simple Kotlin Render
It use swing to create window, and to draw inside it, here is also included a obj file reader, with simple meshes and in low res, it even works fast
![Снимок экрана от 2022-10-15 15-50-28](https://user-images.githubusercontent.com/48290199/195987349-cf312e38-f6da-4d83-bb55-0e3bc6b019f0.png)
because of rendering using swing polygons(in my case, triangles), you can make them wire, or solid
![Снимок экрана от 2022-10-15 15-55-34](https://user-images.githubusercontent.com/48290199/195987516-ab1ff7ad-e220-4858-8cca-1bd8b2356e58.png)
![Снимок экрана от 2022-10-16 21-02-52](https://user-images.githubusercontent.com/48290199/196050846-085c3e98-0868-4873-8929-af07b916fede.png)
to render such wire, i loaded model twice in 2 diferent meshes, second mesh is rendered in wire mode, first normaly
you can use full software rendering, or turn on opengl. OpenGL accelerates only drawing process, not matrix operations, they are still made on cpu
