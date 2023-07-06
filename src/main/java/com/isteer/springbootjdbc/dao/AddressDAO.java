package com.isteer.springbootjdbc.dao;

import java.util.List;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface AddressDAO {

	CustomPostResponse save(List<Address> addresses, Long id);
}
