SELECT 
		i.Name,
		u.LOAD_DTS,
		MAX(u.Value)
	
	FROM 
		HUB_Infrastructure i, 
		SAT_Used u
	WHERE 
		    u.ID_HUB_Infrastructure = i.ID 
        -- AND u.LOAD_DTS = (SELECT MAX(LOAD_DTS) FROM SAT_Used WHERE ID_HUB_Infrastructure = i.ID)
      GROUP by u.LOAD_DTS
      
            
		 
