/**
 * 
 */

let width = 954;

let dx = 10;

let dy = width / 6;

let margin = ({top: 10, right: 120, bottom: 10, left: 40});

let tree = d3.tree().nodeSize([dx, dy]);	

let diagonal = d3.linkHorizontal().x(d => d.y).y(d => d.x);

function makeRoot(data) {
	root = d3.hierarchy(data);
	
	root.x0 = dy / 2;
	root.y0 = 0;
	root.descendants().forEach((d, i) => {
		d.id = i;
		d._children = d.children;
	});
}

function paintTree(source) {

	let nodes = [];
	let links = [];
	
	tree(root);

	let left = root;
	let right = root;
	root.eachBefore(node => {
  		if (node.x < left.x) {
  			left = node;
  		}
  		if (node.x > right.x) {
  			right = node;
  		} 
	});
	
	const svg = d3.select("body")
				  .select("#tree-container")
				  .attr("viewBox", [-margin.left, -margin.top, width, dx])
				  .style("font", "10px sans-serif")
				  .style("user-select", "none");

	const gLink = svg.select("#g-link")
					 .attr("fill", "none")
					 .attr("stroke", "#555")
					 .attr("stroke-opacity", 0.4)
					 .attr("stroke-width", 1.5);

	const gNode = svg.select("#g-node")
					 .attr("fill", "#555")
					 .attr("cursor", "pointer")
					 .attr("pointer-events", "all");

	const height = right.x - left.x + margin.top + margin.bottom;
	
	const transition = svg.transition()
    				  .duration(0)
    				  .attr("viewBox", [-margin.left, left.x - margin.top, width, height])
    				  .tween("resize", window.ResizeObserver ? null : () => () => svg.dispatch("toggle"));
	
	// remove old nodes
	gNode.selectAll("g")
		 .data(nodes, d => d.id)
		 .exit()
		 .remove();
	
	nodes = root.descendants().reverse();
		
	const node = gNode.selectAll("g")
  				  	  .data(nodes, d => d.id);
	
	// new nodes
	const nodeEnter = node.enter().append("g")
    				  .attr("fill-opacity", 0)
    				  .attr("stroke-opacity", 0)
    				  .on("click", d => {
      						d.children = d.children ? null : d._children;
      						paintTree(d);
    				  });

	nodeEnter.append("circle")
    	 	 .attr("r", 3)
         	 .attr("fill", d => d._children ? "#555" : "white")
         	 .attr("stroke-width", 10);

	nodeEnter.append("text")
         	 .attr("dy", "0.31em")
         	 .attr("x", d => d._children ? -6 : 6)
         	 .attr("text-anchor", d => d._children ? "end" : "start")
         	 .text(d => d.data.name)
         	 .clone(true).lower()
         	 .attr("stroke-linejoin", "round")
         	 .attr("stroke-width", 3)
         	 .attr("stroke", "white");

	// update nodes
	const nodeUpdate = node.merge(nodeEnter)
					   .transition(transition)
    				   .attr("transform", d => `translate(${d.y},${d.x})`)
     				   .attr("fill-opacity", 1)
    				   .attr("stroke-opacity", 1);
	
	nodeUpdate.select("circle").attr("fill", d => d._children ? "#555" : "white");
	
	// remove old links
	gLink.selectAll("path")
	  	 .data(links, d => d.target.id)
	  	 .exit()
		 .remove();
	
	links = root.links();
	
	const link = gLink.selectAll("path")
  				  .data(links, d => d.target.id);

	// new links
	const linkEnter = link.enter().append("path")
    				  .attr("d", d => {
      						const o = {x: source.x0, y: source.y0};
      						return diagonal({source: o, target: o});
    				   });
	
	// update links
	link.merge(linkEnter).transition(transition)
    				 .attr("d", diagonal);
}