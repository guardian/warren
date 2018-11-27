import React from 'react';

import Menu from 'components/Menu/Menu';
import MenuItem from 'components/Menu/MenuItem';

const filters = {
	winner: {
		label: 'Single hit',
		fn: values => values.filter(v => v !== '').length === 1,
	},
	conflict: {
		label: 'Conflict',
		fn: values => values.filter(v => v !== '').length > 1,
	},
	loser: {
		label: 'No hits',
		fn: values => values.every(v => v === ''),
	},
};

const Filter = ({ name, checked, label, onSetFilter, counter }) => (
	<MenuItem
		type="radio"
		label={label}
		sublabel={counter}
		checked={checked}
		click={ev => {
			onSetFilter(name);
		}}
	/>
);

const FilterMenu = ({ onSetFilter, selectedFilter, filters, count }) => (
	<Menu>
		<Filter
			name={null}
			checked={!selectedFilter}
			label={'See all'}
			{...{ onSetFilter }}
		/>
		<MenuItem type="separator" />
		{Object.entries(filters).map(([name, { label }]) => (
			<Filter
				key={name}
				checked={name === selectedFilter}
				counter={count[name]}
				{...{ onSetFilter, label, name }}
			/>
		))}
	</Menu>
);

export default FilterMenu;
export { filters };
