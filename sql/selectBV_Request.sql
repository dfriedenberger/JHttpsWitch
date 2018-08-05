SELECT 
		h.Name,      -- Host
		c.ID,        -- Customer
		ct.Type,     -- CustomerType
		r.LOAD_DTS,  -- Date
		r.Url,       -- Url
		r.MimeType,  -- MimeType
		r.Referer    -- Referer
	
	FROM 
		HUB_Host h, 
		HUB_Client c, 
		LNK_Client_to_Host lnk,
		SAT_Request r,
		SAT_ClientType ct
	WHERE 
		 	c.ID = lnk.ID_HUB_Client 
		AND h.ID = lnk.ID_HUB_Host
		
		AND ct.ID_HUB_Client = c.ID 
        AND ct.LOAD_DTS = (SELECT MAX(LOAD_DTS) FROM SAT_ClientType WHERE ID_HUB_Client = c.ID)
        
		AND r.ID_LNK_Client_to_Host = lnk.ID 
        
        AND r.MimeType = 'text/html'
     
      
            
		 
