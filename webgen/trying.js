let module = document.getElementById("module").innerHTML;

if (module === "commitActivity") {
    commitActivity();

}else if (module === "contributorActivity") {
    contributorActivity();
}



function commitActivity() {
    let grid = document.getElementById("days");
    let bubble = document.getElementById("bubble");

    let child = grid.children;

    for (let i = 0; i<child.length; i++) {
        let multi = child[i].getAttribute("data-commit-number");
        console.log(multi);
        child[i].style.background = "rgb("+ multi * 20 + "," + multi * 50 + "," + multi * 20+ ")";
    }

    for (let i = 0; i<child.length; i++) {
        child[i].onmouseover = (element) => {
            bubble.style.top = element.target.getBoundingClientRect().y - 40 + "px";
            bubble.style.left = element.target.getBoundingClientRect().x -15 + "px";
            bubble.innerHTML = element.target.getAttribute("data-date") + " : " + element.target.getAttribute("data-commit-number") + " commits";
            bubble.removeAttribute("hidden"); 
        }
        child[i].onmouseleave = () => {
            bubble.setAttribute("hidden", ""); 
        }
    }
}

function contributorActivity() {
    let data = document.getElementById("data-contributor-activity");
    let dataObject = convertToObject(data);

    let description = document.getElementById("project-stat-desc").firstElementChild
    description.innerHTML = "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués.";

    let canvasParent = document.getElementById("project-stat-display");
    let canvasDiv = document.createElement("canvas");
    canvasParent.appendChild(canvasDiv);
    
    let myChart = new Chart(canvasDiv, {
        type: 'bar',
        data: {
            labels: dataObject.contributors,
            datasets: [{
                label: '# of Commit',
                data: dataObject.commitNumbers,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 1
            }],   
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    display: true,
                    ticks: {
                        beginAtZero: true,
                        min: 0
                    }
                }],
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    })
}

function convertToObject(document) {
    let child = document.children;
    let data = {
        'contributors' : [],
        'commitNumbers': []
    }
    for (let i = 0; i<child.length; i++) {
        data.contributors.push(child[i].getAttribute("data-contributor-name"));
        data.commitNumbers.push(parseInt(child[i].innerHTML));
    }
    return data;
}