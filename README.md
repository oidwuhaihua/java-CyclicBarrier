
## java CyclicBarrier 的作用  
通常有必要编写一个程序，其中可能是多线程的，大多数线程是独立的，但是偶尔继续之前需要同步。这种应用场景使用**CyclicBarrier**
还是不错的。**CyclicBarrier** 被称为**屏障**，它可以阻止多个线程进行,一旦多个线程到达屏障，再全部释放。然后可以再重新使用
直到条件结束！

## 使用 CyclicBarrier
当使用 屏障时，需要指定屏障线程线程数，通常用这个数字专业术语叫party(聚会)，就像线程聚会一样？
1、指定你要**party**的数。
2、调用**await**屏障。

> 注意：所有调用**await** 将被阻塞，直到n次线程调用。其中n是创建屏障的指定数。
> 重要的是与屏障协调的线程数量一定要大于或者等于**party**的数量，如果没有，所有线程将阻塞，你的应用程序永远不会终止。

## 快速案列
我们来想象一个场景，有4个朋友喜欢去旅行，然后每个人都通过自己的方式去指定的地方玩，在这4个人当中，有徒步去的，有搭公交去的，有
做飞机去的，有坐火车去的。做飞机的总是是能够先飞到目的地，而徒步的每次都是最后到达，但是它们最终会到目的地集合。
在夏天它们决定去邵阳玩，为了确保每个人都安全，他们一旦到达目的地都会等待其他人的到达，然后一起游玩，再一起以自己的方式出发下一个城市。

代码场景体现如下所示：
```java
public class BarruerExameple {
    public static  String[] locations ={"邵阳", "上海", "广州","深圳",
                                 "New York","Albany"};
    public static CyclicBarrier barrier;

    public static void main(String[] args) {
        Thread walker = new Thread(getRunnable("walker",5));
        Thread bus = new Thread(getRunnable("bus",4));
        Thread train = new Thread(getRunnable("train",3));
        Thread plane = new Thread(getRunnable("plane",2));
        barrier = new CyclicBarrier(4);
        walker.start();
        bus.start();
        train.start();
        plane.start();
    }
    public static Runnable getRunnable(final String type,final int secs){

        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread " + type + " starting trip");
                for (int i = 0 ;i<locations.length; i++){
                        try {
                            Thread.sleep(secs * 1000);//旅行个所需要的时间
                            System.out.println("Thread " + type + " 到达 : " + locations[i]);
                            System.out.println("Thread " + type + " 坐等其他朋友...");
                            barrier.await();
                            System.out.println("Thread " + type + " 一起离开: " + locations[i]);
                            }
                            catch(InterruptedException ie) {
                                System.out.println("Thread " + type + " interrupted");
                            } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                }
                System.out.println("Thread " + type + " finished trip!");
            }
        };

    }
}
```
## 定义的Runnable
在该例中**Runnable**代表每个人:
```java
public static Runnable getRunnable(final String type,final int secs){
```
该**type**主要用来区分每个线程，**secs**表示每个线程的睡眠时间。这里我们模拟了每个人到达一个城市花费的时间，
比如飞机最快(sleep 2秒)，而步行者最慢花费了5秒。我们初始化定义**CyclicBarrier**屏障 4个线程：
```java
Thread walker = new Thread(getRunnable("walker",5));
Thread bus = new Thread(getRunnable("bus",4));
Thread train = new Thread(getRunnable("train",3));
Thread plane = new Thread(getRunnable("plane",2));
barrier = new CyclicBarrier(4);
```
## 等待朋友一起通过屏障
每个人到达一个城市不能立即前往下一个城市，直到所有人的到达。每个线程都要调用**await**,确保每个人到位！才能一起前往下个城市！
```java
Thread.sleep(secs * 1000);//旅行个所需要的时间
System.out.println("Thread " + type + " 到达 : " + locations[i]);
System.out.println("Thread " + type + " 坐等其他朋友...");
barrier.await();
```
到这里例子已经结束了！不知道有没有你适合的业务场景呢？
