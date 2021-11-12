let modules = document.getElementsByClassName("module");
let moduleNumber = 0;

for (i = 0; i<modules.length; i++) {
    if(modules[i].innerHTML === "commitActivity") {commitActivity(modules[i]); moduleNumber++;}
    if(modules[i].innerHTML === "contributorActivity") {contributorActivity(modules[i]); moduleNumber++;}
}



function commitActivity(module) {
    let moduleInfo = {
        'name': "commit Activity",
        'desc': "Graphique représentant le nombre de commit effectué par jour"
    }
    let moduleNext = module.nextElementSibling;
    console.log(moduleNext);
    let divs = placeTemplate(moduleInfo, [module, moduleNext]);

    let grid = document.getElementsByClassName("days")[moduleNumber];
    let bubble = document.getElementById("bubble");

    let child = grid.children;

    let gridContainer = document.getElementById('stats-grid');
    let dayListContainer = document.getElementById('day-list');
    let takedWidth = gridContainer.clientWidth + dayListContainer.clientWidth;
    let freeWidth = moduleNext.clientWidth;
    console.log(takedWidth, freeWidth);
    if (takedWidth >= freeWidth) {
        let parent = gridContainer.parentElement.parentElement
        parent.style.borderBottomLeftRadius = "0px";
        parent.style.borderBottomRightRadius = "0px";
        parent.addEventListener('wheel', (event) => {
            parent.scrollLeft += (event.deltaY * 1.2);
            event.preventDefault();
        })
    }

    for (let i = 0; i<child.length; i++) {
        let multi = child[i].getAttribute("data-commit-number");
        if (multi > 5) {multi = 5;}
        child[i].style.background = "rgb("+ multi * 20 + "," + multi * 50 + "," + multi * 20+ ")";
    }

    for (let i = 0; i<child.length; i++) {
        child[i].onmouseover = (element) => {
            bubble.removeAttribute("hidden");
            bubble.style.top = element.target.getBoundingClientRect().y - 40 + "px";
            bubble.style.left = element.target.getBoundingClientRect().x -15 + "px";
            bubble.style.opacity=1;

            bubble.innerHTML = element.target.getAttribute("data-date") + " : " + element.target.getAttribute("data-commit-number") + " commits";
        }
        child[i].onmouseleave = () => {
            bubble.setAttribute("hidden", "");
            bubble.style.opacity=0;
        }
    }
}

function contributorActivity(module) {
    let moduleInfo = {
        'name': "Contributor Activity",
        'desc': "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués."
    }
    let divs = placeTemplate(moduleInfo, [module, module.nextSibling]);
    let data = document.getElementById("data-contributor-activity");
    let dataObject = convertToObject(data);

    if (divs == null) {
        let description = document.getElementsByClassName("project-stat-desc")[moduleNumber].firstElementChild
        description.innerHTML = "Graphique représentant l'activité de chaque contributeurs.\n\n Ce graphique se base sur le nombres de commit effectués.";
    }

    let canvasParent = document.getElementsByClassName("project-stat-display")[moduleNumber];
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

function createProjectInfoDiv(moduleName, moduleDesc) {
    let projectInfo = document.createElement('div');
    projectInfo.classList.add('project-info');

    let projectDescContainer = document.createElement('div');
    let projectStatContainer = document.createElement('div');
    projectDescContainer.classList.add('project-desc-container');
    projectStatContainer.classList.add('project-stat-container');


    let projectStatDesc = document.createElement('div');
    let projectStat = document.createElement('div');
    let projectStatDisplay = document.createElement('div');
    projectStatDesc.classList.add('project-stat-desc');
    projectStat.classList.add('project-stat');
    projectStatDisplay.classList.add('project-stat-display');

    projectInfo.appendChild(projectDescContainer);
    projectInfo.appendChild(projectStatContainer);

    let title = document.createElement('h2');
    title.innerHTML = moduleName;
    projectStatContainer.appendChild(title);
    projectStatContainer.appendChild(projectStatDesc);
    projectStatContainer.appendChild(projectStat);

    let desc = document.createElement('p');
    desc.innerHTML = moduleDesc;
    projectStatDesc.appendChild(desc);

    projectStat.appendChild(projectStatDisplay);

    let returnObject = {
        'projectInfo': projectInfo,
        'projectStatDisplay': projectStatDisplay
    }

    return returnObject;    
}

function placeTemplate(moduleInfo, element) {
    console.log(element);
    if(moduleNumber == 0) {return null;}
    let container = document.getElementById('container');
    let divs = createProjectInfoDiv(moduleInfo.name, moduleInfo.desc);
    for (i = 0; i<element.length; i++) {
        divs.projectStatDisplay.appendChild(element[i]);
    }
    container.appendChild(divs.projectInfo);
    return divs;
}