var label_path = {
    ko:{
        "TG-ih": '타겟 저해(Target Inhibitor)',
        "TG-id": '타겟 유도(Target Inducer)',
        "TG-ss": '타겟 기질(Target Substrate)',
        "TR-ih": '수송체 저해(Transporter Inhibitor)',
        "TR-id": '수송체 유도(Transporter Inducer)',
        "TR-ss": '수송체 기질(Transporter Substrate)',
        "EZ-ih": '효소 저해(Enzyme Inhibitor)',
        "EZ-id": '효소 유도(Enzyme Inducer)',
        "EZ-ss": '효소 기질(Enzyme Substrate)',
        "CR-ih": '운반체 저해(Carrier Inhibitor)',
        "CR-id": '운반체 유도(Carrier Inducer)',
        "CR-ss": '운반체 기질(Carrier Substrate)'
    },
    en:{
        "TG-ih": 'Target Inhibitor',
        "TG-id": 'Target Inducer',
        "TG-ss": 'Target Substrate',
        "TR-ih": 'Transporter Inhibitor',
        "TR-id": 'Transporter Inducer',
        "TR-ss": 'Transporter Substrate',
        "EZ-ih": 'Enzyme Inhibitor',
        "EZ-id": 'Enzyme Inducer',
        "EZ-ss": 'Enzyme Substrate',
        "CR-ih": 'Carrier Inhibitor',
        "CR-id": 'Carrier Inducer',
        "CR-ss": 'Carrier Substrate'
    },
	es:{
		"TG-ih": 'Objetivo Inhibidor(Target Inhibitor)',
        "TG-id": 'Objetivo Inductor(Target Inducer)',
        "TG-ss": 'Objetivo Sustrato(Target Substrate)',
        "TR-ih": 'Transportador Inhibidor(Transporter Inhibitor)',
        "TR-id": 'Transportador Inductor(Transporter Inducer)',
        "TR-ss": 'Transportador Sustrato(Transporter Substrate)',
        "EZ-ih": 'Enzima Inhibidor(Enzyme Inhibitor)',
        "EZ-id": 'Enzima Inductor(Enzyme Inducer)',
        "EZ-ss": 'Enzima Sustrato(Enzyme Substrate)',
        "CR-ih": 'Portador Inhibidor(Carrier Inhibitor)',
        "CR-id": 'Portador Inductor(Carrier Inducer)',
        "CR-ss": 'Portador Sustrato(Carrier Substrate)'
	}
}

var colors_path = {
    "TG-ih": '#0033cc',
    "TG-id": '#0033cc',
    "TG-ss": '#b3b300',
    "TR-ih": '#b300b3',
    "TR-id": '#b300b3',
    "TR-ss": '#b3b300',
    "EZ-ih": '#cc0000',
    "EZ-id": '#cc0000',
    "EZ-ss": '#b3b300',
    "CR-ih": '#00b300',
    "CR-id": '#00b300',
    "CR-ss": '#b3b300'
}

function drawNet() {
    var addNode = function (n, t) {
        if (!findNode(n.id)) {
            nodes.push({ "id": n.id, "label": n.label, "type": t });
            update();
        }
    };

    var addLink = function (source, target, value, type) {
        links.push({ "source": findNode(source.id), "target": findNode(target.id), "value": value, "type": type });
        update();
    };

    this.initialize = function (datas) {
        datas.forEach(function (d) {
            var a = 'd', b = 'p';
            if (d.type == 'p') {
                a = 'p'; b = 'd';
            }
            addNode(d.source, a);
            addNode(d.target, b);
            addLink(d.source, d.target, d.value, d.type);


            linkedByIndex[d.source.id + "," + d.target.id] = 1;
            linkedByIndex[d.target.id + "," + d.source.id] = 1;
        });
    };

    function neighboring(a, b) {
        return a.id == b.id || linkedByIndex[a.id + "," + b.id];
    }
    
    function mouseover(d) {
        d3.selectAll(".link").transition().duration(500)
            .style("opacity", function (o) {
                return o.source === d || o.target === d ? 1 : .1;
            });
    
        d3.selectAll(".node").transition().duration(500)
            .style("opacity", function (o) {
                return neighboring(d, o) ? 1 : .1;
            });

        d3.selectAll(".pathLabel").transition().duration(500)
            .style("opacity", function (o) {
                return o.source === d || o.target === d ? 1 : .1;
            });
    }
    
    function mouseout() {
        d3.selectAll(".link").transition().duration(500).style("opacity", 1);
        d3.selectAll(".node").transition().duration(500).style("opacity", 1);
        d3.selectAll(".pathLabel").transition().duration(500).style("opacity", 1);
    }    

    var findNode = function (nodeId) {
        for (var i in nodes) {
            if (nodes[i].id === nodeId) {
                return nodes[i];
            }
        };
    };

    var countSiblingLinks = function (source, target) {
        var count = 0;
        for (var i = 0; i < links.length; ++i) {
            if ((links[i].source == source && links[i].target == target.id) || (links[i].source.id == target.id && links[i].target.id == source.id))
                count++;
        };
        return count;
    };

    var getSiblingLinks = function (source, target) {
        var siblings = [];
        for (var i = 0; i < links.length; ++i) {
            if ((links[i].source.id == source.id && links[i].target.id == target.id) || (links[i].source.id == target.id && links[i].target.id == source.id))
                siblings.push(links[i].value);
        };
        return siblings;
    };

    var w = window.innerWidth - 20,
        h = window.innerHeight - 20;

    var linkedByIndex = {};

    var colors_node = {
        "d": "red",
        "p": "green"
    }

    var zoom = d3.behavior.zoom()
        .scaleExtent([.5, 10])
        .on("zoom", zoomed);

    function zoomed() {
        svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
    }

    var svg = d3.select("#net")
        .append("svg:svg")
        .attr("width", w)
        .attr("height", h)
        .style("z-index", -10)
        .attr("id", "svg")
        .call(zoom).on("dblclick.zoom", null).append('svg:g');



    var force = d3.layout.force().charge(-300);

    var nodes = force.nodes(),
        links = force.links();

    function marker(color) {
        svg.append('svg:defs').append("svg:marker")
            .attr("id", color.replace("#", ""))
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 15) // This sets how far back it sits, kinda
            .attr("refY", 0)
            .attr("markerWidth", 9)
            .attr("markerHeight", 9)
            .attr("orient", "auto")
            .attr("markerUnits", "userSpaceOnUse")
            .append("svg:path")
            .attr("d", "M0,-5L10,0L0,5")
            .style("fill", color);

        return "url(" + color + ")";
    };

    var update = function () {

        var drag = d3.behavior.drag()
        .origin(function (d) { return d; })
        .on("dragstart", dragstarted)
        .on("drag", dragged)
        .on("dragend", dragended);


        var path = svg.selectAll("path.link")
            .data(force.links());

        path.enter().append("svg:path")
            .attr("id", function (d) {
                return d.source.id + "-" + d.value + "-" + d.target.id;
            })
            .attr("class", function (d) {
                if (d.type == 'p') return "link link_dot"
                return "link";
            })
            .attr('marker-end', 'url(#arrowhead)')
            .each(function (d) {
                var color = colors_path[d.value];
                d3.select(this).style("stroke", color)
                    .attr("marker-end", marker(color));
            })

        path.exit().remove();

        var pathInvis = svg.selectAll("path.invis")
            .data(force.links());

        pathInvis.enter().append("svg:path")
            .attr("id", function (d) {
                return "invis_" + d.source.id + "-" + d.value + "-" + d.target.id;
            })
            .attr("class", "invis");

        pathInvis.exit().remove();

        var pathLabel = svg.selectAll(".pathLabel")
            .data(force.links());

        pathLabel.enter().append("g").append("svg:text")
            .attr("class", "pathLabel")
            .append("svg:textPath")
            .attr("startOffset", "50%")
            .attr("text-anchor", "middle")
            .attr("xlink:href", function (d) { return "#invis_" + d.source.id + "-" + d.value + "-" + d.target.id; })
            .style("fill", function (d) {
                return colors_path[d.value];
            })
            .style("font-size", 12)
            .text(function (d) { return d.value; });

        var node = svg.selectAll("g.node")
            .data(force.nodes());

        var nodeEnter = node.enter().append("g")
            .attr("class", "node")
            .call(drag);

        nodeEnter.append("svg:circle")
            .attr("r", function (d) {
                if (d.type == 'd') return 8;
                return 5;
            })
            .attr("id", function (d) {
                return "Node;" + d.id;
            })
            .attr("class", "nodeStrokeClass")
            .style("fill", function (d) { return colors_node[d.type]; })
            .on("mouseover", mouseover)
            .on("mouseout", mouseout)

        nodeEnter.append("svg:text")
            .attr("class", "textClass")
            .attr("x", 15)
            .attr("y", ".31em")
            .text(function (d) {
                return d.label;
            });

        node.exit().remove();

        node.on("click", function (d) { d.fixed = true; });

        node.on("dblclick", function (d) { d.fixed = false; });

        function dragstarted(d) {
            d3.event.sourceEvent.stopPropagation();
            d.fixed |= 2;
        }
        function dragged(d) {
            var mouse = d3.mouse(svg.node());
            d.x = mouse[0]; //xScale.invert(mouse[0]);
            d.y = mouse[1]; //yScale.invert(mouse[1]);
            d.px = d.x;
            d.py = d.y;
            force.resume();
        }
    
        function dragended(d) {
            d.fixed &= ~6;
        }

        function arcPath(leftHand, d) {
            /*
            if(d.type == 'd') {
                var dr = 0;
                return "M" + d.source.x + "," + d.source.y +
                "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
            }*/

            var x1 = leftHand ? d.source.x : d.target.x,
                y1 = leftHand ? d.source.y : d.target.y,
                x2 = leftHand ? d.target.x : d.source.x,
                y2 = leftHand ? d.target.y : d.source.y,
                dx = x2 - x1,
                dy = y2 - y1,
                dr = Math.sqrt(dx * dx + dy * dy),
                drx = dr,
                dry = dr,
                sweep = leftHand ? 0 : 1;
            siblingCount = countSiblingLinks(d.source, d.target)
            xRotation = 0,
                largeArc = 0;

            if (siblingCount > 1) {
                var siblings = getSiblingLinks(d.source, d.target);
                var arcScale = d3.scale.ordinal()
                    .domain(siblings)
                    .rangePoints([1, siblingCount]);
                drx = drx / (1 + (1 / siblingCount) * (arcScale(d.value) - 1));
                dry = dry / (1 + (1 / siblingCount) * (arcScale(d.value) - 1));
            }

            return "M" + x1 + "," + y1 + "A" + drx + ", " + dry + " " + xRotation + ", " + largeArc + ", " + sweep + " " + x2 + "," + y2;
        }

        force.on("tick", function (e) {
            var q = d3.geom.quadtree(nodes),
                i = 0,
                n = nodes.length,
                k = .1 * e.alpha;

            while (++i < n) q.visit(collide(nodes[i]));

            node.attr("transform", function (d) {
                return "translate(" + d.x + "," + d.y + ")";
            });

            path.attr("d", function (d) {
                return arcPath(true, d);
            });

            pathInvis.attr("d", function (d) {
                return arcPath(d.source.x < d.target.x, d);
            });
        });


        var linkDistance = 300;

        force
            .charge(-1300)
            .friction(0.2)
            .linkDistance(linkDistance)
            .gravity(0.01)
            .size([w, h])
            .start();
        keepNodesOnTop();
    }

    update();

    function collide(node) {
        var r = node.radius + 16,
            nx1 = node.x - r,
            nx2 = node.x + r,
            ny1 = node.y - r,
            ny2 = node.y + r;
        return function (quad, x1, y1, x2, y2) {
            if (quad.point && (quad.point !== node)) {
                var x = node.x - quad.point.x,
                    y = node.y - quad.point.y,
                    l = Math.sqrt(x * x + y * y),
                    r = node.radius + quad.point.radius;
                if (l < r) {
                    l = (l - r) / l * .5;
                    node.x -= x *= l;
                    node.y -= y *= l;
                    quad.point.x += x;
                    quad.point.y += y;
                }
            }
            return x1 > nx2 || x2 < nx1 || y1 > ny2 || y2 < ny1;
        };
    }
}

function keepNodesOnTop() {
    $(".nodeStrokeClass").each(function (index) {
        var gNode = this.parentNode;
        gNode.parentNode.appendChild(gNode);
    });
}