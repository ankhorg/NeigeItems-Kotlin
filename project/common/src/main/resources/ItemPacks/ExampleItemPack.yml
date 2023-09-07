Example1:
  Items:
    # 支持解析即时声明节点
    # [物品ID] (数量(或随机最小数量-随机最大数量)) (生成概率) (是否反复随机) (指向数据)
    - ExampleItem 1-5 0.5
    - test
  FancyDrop:
    # 偏移量
    offset:
      # 横向偏移量(或随机最小偏移量-随机最大偏移量)
      x: 0.1
      # 纵向偏移量(或随机最小偏移量-随机最大偏移量)
      y: 0.8
    angle:
      # 抛射类型(round/random)
      type: round
Example2:
  Items:
    - <test>
  FancyDrop:
    offset:
      x: 0.1
      y: 0.8
    angle:
      type: round
  # 引用的全局节点
  globalsections:
    # 这种直接填写文件名的方式可以直接调用文件内的全部全局节点
    # - ExampleSection.yml
    - global-strings-1
    - global-number-1
  # 物品私有节点
  sections:
    test:
      type: strings
      values:
        - ExampleItem 5 1
        - ExampleItem 10 1