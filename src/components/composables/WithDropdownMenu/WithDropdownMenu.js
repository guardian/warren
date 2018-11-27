import React from 'react';

import WithPopOver from 'components/composables/WithPopOver/WithPopOver';
import Menu from 'components/Menu/Menu';

const WithDropdownMenu = ({ children, ...rest }) => {
	return (
		<WithPopOver {...rest}>
			<Menu isPopover>{children}</Menu>
		</WithPopOver>
	);
};

export default WithDropdownMenu;
