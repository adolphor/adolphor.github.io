---
layout:     post
title:      【转】Java有限状态机的4种实现对比
date:       2023-12-14 17:42:55 +0800
postId:     2023-12-14-17-42-55
categories: [Microservice]
keywords:   [Microservice]
---

在日常工作过程中，我们经常会遇到状态的变化场景，例如订单状态发生变化，商品状态的变化。
这些状态的变化，我们称为有限状态机，缩写为FSM(Finite State Machine)。之所以称其为有限，
是因为这些场景中的状态往往是可以枚举出来的有限个的，所以称其为有限状态机。

## 基础概念
一个状态机主要包含以下三个元素：  
* 状态（States）
* 事件（Events）
* 转换（Transitions）

### 状态（States） 
这是系统可能存在的条件或情况。在你的代码中，EntranceMachineState 枚举定义了状态机的所有可能状态，包括 UNLOCKED 和 LOCKED。  

### 事件（Events）
这是触发状态转换的行为或发生的事情。在你的代码中，Action 枚举定义了所有可能的事件，包括 INSERT_COIN 和 PASS。  

### 转换（Transitions）
这描述了系统从一个状态到另一个状态的过程。在你的代码中，EntranceMachine 类的 execute 方法处理了状态转换的逻辑。  

除此之外，状态机可能还包含一些其他元素，如初始状态、终止状态等，具体取决于状态机的具体应用场景。

## 场景范例
下面我们来看一个具体的场景例子。

> 简单场景：
> 地铁进站闸口的状态有两个：已经关闭、已经开启两个状态。
> 刷卡后闸口从已关闭变为已开启，人通过后闸口状态从已开启变为已关闭。


### 实现方式
遇到这类问题，在编码时我们应该如何处理呢？一般来说，我们可以有以下几种实现方式：
* 基于Switch
* 基于状态集合
* 基于State模式
* 基于枚举的实现

下面我们针对每一种实现方式进行分析。场景分解后会有一下2种状态4种情况出现：

|Index|	State|	Event|	NextState|	Action
|---|---|---|---|---|
|1	|闸机口 | LOCKED |	投币 |	闸机口 UN_LOCKED |	闸机口打开闸门 |
|2	|闸机口 | LOCKED |	通过 |	闸机口 LOCKED |	闸机口警告 |
|3	|闸机口 | UN_LOCKED |	投币 |	闸机口 UN _LOCKED |	闸机口退币 |
|4	|闸机口 | UN_LOCKED |	通过 |	闸机口 LOCKED |	闸机口关闭闸门 |

### Test Case
针对以上4种请求，共拆分了5个Test Case：

<table>
<thead>
<tr>
 <th>Case NO.</th>
 <th>Detail</th>
</tr>
</thead>
<tbody>
<tr>
<td>Test Case 01</td>
<td>
<li> Given：一个Locked的进站闸口<br> </li>
<li> When: 投入硬币<br> </li>
<li> Then：打开闸口 </li>
</td>
</tr>
<tr>
<td>Test Case 02</td>
<td>
<li> Given：一个Locked的进站闸口<br> </li>
<li> When: 通过闸口<br> </li>
<li> Then：警告提示 </li>
</td>
</tr>
<tr>
<td>Test Case 03</td>
<td>
<li> Given：一个Unocked的进站闸口<br> </li>
<li> When: 通过闸口<br> </li>
<li> Then：闸口关闭 </li>
</td>
</tr>
<tr>
<td>Test Case 04</td>
<td>
<li> Given：一个Unlocked的进站闸口<br> </li>
<li> When: 投入硬币<br> </li>
<li> Then：退还硬币 </li>  
</td>
</tr>
<tr>
<td>Test Case 05</td>
<td>
<li> Given：一个闸机口<br></li>
<li> When: 非法操作<br></li>
<li> Then：操作失败  </li>
</td>
</tr>
</tbody>
</table>

## 使用Switch来实现有限状态机

这种方式只需要懂得Java语法及可以实现出来。先看代码，然后我们在讨论这种实现方式是否好。

### 基础类
> [Action.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d14/fsm/swch/Action.java)

```java
public enum Action {
    INSERT_COIN,
    PASS
}
```

> [EntranceMachineState.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d14/fsm/swch/EntranceMachineState.java)

```java
public enum EntranceMachineState {
    LOCKED,
    UNLOCKED
}
```

> [InvalidActionException.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d14/fsm/swch/InvalidActionException.java)

```java
public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message) {
        super(message);
    }
}
```

### 核心实现

> [EntranceMachine.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d14/fsm/swch/EntranceMachine.java)

```java
@Data
public class EntranceMachine {
    private EntranceMachineState state;

    public EntranceMachine() {
        this.state = EntranceMachineState.LOCKED;
    }

    public void execute(Action action) {
        switch (state) {
            case LOCKED:
                switch (action) {
                    case INSERT_COIN:
                        state = EntranceMachineState.UNLOCKED;
                        System.out.println("闸机口打开闸门");
                        break;
                    case PASS:
                        System.out.println("闸机口警告");
                        break;
                    default:
                        throw new InvalidActionException("非法操作");
                }
                break;
            case UNLOCKED:
                switch (action) {
                    case INSERT_COIN:
                        System.out.println("闸机口退币");
                        break;
                    case PASS:
                        state = EntranceMachineState.LOCKED;
                        System.out.println("闸机口关闭闸门");
                        break;
                    default:
                        throw new InvalidActionException("非法操作");
                }
                break;
            default:
                throw new InvalidActionException("非法操作");
        }
    }
}
```

### 单元测试

> [EntranceMachineTest.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d14/fsm/swch/EntranceMachineTest.java)

```java
public class EntranceMachineTest {
    @Test
    public void should_unlocked_when_insert_coin_given_locked() {
        EntranceMachine entranceMachine = new EntranceMachine();
        entranceMachine.execute(Action.INSERT_COIN);
        assertEquals(EntranceMachineState.UNLOCKED, entranceMachine.getState());
    }

    @Test
    public void should_warn_when_pass_given_locked() {
        EntranceMachine entranceMachine = new EntranceMachine();
        entranceMachine.execute(Action.PASS);
        assertEquals(EntranceMachineState.LOCKED, entranceMachine.getState());
    }

    @Test
    public void should_close_when_pass_given_unlocked() {
        EntranceMachine entranceMachine = new EntranceMachine();
        entranceMachine.setState(EntranceMachineState.UNLOCKED);
        entranceMachine.execute(Action.PASS);
        assertEquals(EntranceMachineState.LOCKED, entranceMachine.getState());
    }

    @Test
    public void should_refund_when_insert_coin_given_unlocked() {
        EntranceMachine entranceMachine = new EntranceMachine();
        entranceMachine.setState(EntranceMachineState.UNLOCKED);
        entranceMachine.execute(Action.INSERT_COIN);
        assertEquals(EntranceMachineState.UNLOCKED, entranceMachine.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void should_throw_exception_when_invalid_action_given_any_state() {
        EntranceMachine entranceMachine = new EntranceMachine();
        entranceMachine.execute(null);
    }
}
```

if(), swich语句都是switch语句，但是[Switch是一种Code Bad Smell](https://www.kancloud.cn/sstd521/refactor/194220)，
因为它本质上一种重复。当代码中有多处相同的switch时，会让系统变得晦涩难懂，脆弱，不易修改。

上面的代码虽然出现了多层嵌套但是还算是结构简单，不过想通过并不能很清楚闸机口的逻辑还是化点时间。
如果闸机口的状态等多一些，那就阅读、理解起来也就更加困难。

所以在日常工作，我遵循“事不过三，三则重构”的原则：
* 当只有一两个状态（或者重复）时，那么先用最简单的实现实现。
* 一旦出现三种以及以上的状态（或者重复），立即重构。

## State模式







## From Copilot

**状态机**，或者称为**有限状态机**（Finite State Machine, **FSM**），既不是算法也不是设计模式，而是一种用来描述系统行为的模型。
状态机在计算机科学和数学中被广泛使用，它可以用来表示一个系统在任何给定时间的状态，以及该系统如何根据内部和外部事件从一个状态转移到另一个状态。 

状态机主要由状态（States）、事件（Events）和转换（Transitions）三部分组成。状态是系统可能存在的条件或情况。
事件是触发状态转换的行为或发生的事情。转换描述了系统从一个状态到另一个状态的过程。 

在编程中，状态机常常被用来解决需要处理多种状态和状态转换的问题，例如网络协议的实现、游戏角色的行为控制、工作流管理等。

## 参考资料
* [【转】Java有限状态机的4种实现对比](https://zhuanlan.zhihu.com/p/97442825)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/12/14/02/xxx.png)
```
