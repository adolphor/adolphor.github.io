

* [如何高效地遍历 MongoDB 超大集合？](https://blog.fundebug.com/2019/03/21/how-to-visit-all-documents-in-a-big-collection-of-mongodb/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)
  - 将整个集合 find()全部返回，这种操作应该避免，正确的方法应该是这样的：使用cursor()方法返回 QueryCursor，然后再使用eachAsync()就可以遍历整个集合了，而且不用担心内存不够。


