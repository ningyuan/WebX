/**
 * 
 */

let data = [
		   		{id: "0", value: 19912018},
		   		{id: "1", value: 20501982},
		   		{id: "2", value: 20679786},
		   		{id: "3", value: 21354481},
		   		{id: "4", value: 22604232},
		   		{id: "5", value: 21698010},
		   		{id: "6", value: 21183639},
		   		{id: "7", value: 19855782},
		   		{id: "8", value: 20796128},
		   		{id: "9", value: 21370368},
		   		{id: "10", value: 22525490},
		   		{id: "11", value: 21001947},
		   		{id: "12", value: 18415681},
		   		{id: "13", value: 14547446},
		   		{id: "14", value: 10587721}
		   	];

let color = d3.scaleOrdinal()
			  .domain(data.map(d => d.id))
			  .range(d3.quantize(t => d3.interpolateSpectral(t * 0.8 + 0.1), data.length).reverse());

let width = 1000;
let height = 200;
 
let arc = d3.arc().innerRadius(Math.min(width, height) / 2 * 0.67).outerRadius(Math.min(width, height) / 2 - 1);
		  
let pie = d3.pie()
		    .padAngle(0.005)
		    .sort(null)
		    .value(d => d.value);
