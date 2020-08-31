SELECT id INTO @id FROM `category` WHERE NAME = '虾类';
INSERT INTO `category`(parent_id, NAME, full_name, LEVEL, created, modified)
  VALUES (@id,'虾蛄','虾类,虾蛄',2,NOW(),NOW());