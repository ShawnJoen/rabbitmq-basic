# RabbitMq Producer And Consumer
&nbsp;&nbsp;&nbsp;&nbsp;·&nbsp;有多个Consumer时 Producer发送一个消息 只能其中一个Consumer(随机空闲的)可以收到消息。 <br><br>
&nbsp;&nbsp;&nbsp;&nbsp;·&nbsp;一个消息只能消费一次,消费后销毁。 <br><br>
&nbsp;&nbsp;&nbsp;&nbsp;·&nbsp;未消费的RabbitMq缓存留着等待消费。
# RabbitMq Publish(发布) And Subscribe(订阅)
&nbsp;&nbsp;&nbsp;&nbsp;·&nbsp;有多个Subscribe(Consumer)时 Publish(Producer)发送一个消息 每个Subscribe(Consumer)都可以收到。<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;·&nbsp;发送时无一个订阅者 阅读也是不会留着 未消费过一次也会消失。<br><br>
# RabbitMq 发布/订阅按转发器类型[Direct, Topic, Fanout]
&nbsp;&nbsp;&nbsp;Fanout&nbsp;:&nbsp;广播到所有它所知道的队列, 条件为 指定的所有转发器。<br><br>
&nbsp;&nbsp;&nbsp;Direct&nbsp;:&nbsp;广播到所有它所知道的队列, 条件为 指定的所有转发器内指定路由。<br><br>
&nbsp;&nbsp;&nbsp;Topic&nbsp;:&nbsp;广播到所有它所知道的队列, 条件为 指定的所有转发器内指定路由(可以使用标识符来筛选路由)。<br><br>
