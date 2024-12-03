const chartArea = document.querySelector('#chartArea');

const sDate = document.querySelector('INPUT[name=sdate]');
const eDate = document.querySelector('INPUT[name=edate]');
const sMonth = document.querySelector('INPUT[name=smonth]');
const eMonth = document.querySelector('INPUT[name=emonth]');
const category3 = document.querySelector('#category3');
const daysType = document.querySelector('#daysType');

const initDaySelect = () => {
    const today = new Date();
    eDate.value = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`;
    today.setDate(today.getDate() - 7);
    sDate.value = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`;
}

const initMonthSelect = () => {
    const today = new Date();
    sMonth.value = `${today.getFullYear()}-${today.getMonth() + 1}`;
    eMonth.value = `${today.getFullYear()}-${today.getMonth() + 1}`;
}

document.querySelector('#chartType').addEventListener('change', (event) => {
    const daysContent = document.querySelector('.days-content');
    const weekdayContent = document.querySelector('.weekday-content');
    const chartType = event.target.value;
    if(chartType === 'WEEKDAY') {
        daysContent.classList.add('d-none');
        weekdayContent.classList.remove('d-none');
        getAnalyticsData();
    } else {
        weekdayContent.classList.add('d-none');
        daysContent.classList.remove('d-none');
        getAnalyticsData();
    }
});

daysType.addEventListener('change', (event) => {
    const daySelect = document.querySelector('.daySelect');
    const monthSelect = document.querySelector('.monthSelect');

    daySelect.classList.toggle('d-none');
    monthSelect.classList.toggle('d-none');
})

document.querySelector('#map').addEventListener('change', () => getAnalyticsData());
category3.addEventListener('change', () => getAnalyticsData());
daysType.addEventListener('change', () => getAnalyticsData());
document.querySelector('#btnDataExport').addEventListener('click', () => downloadData());

const getAnalyticsData = () => {
    const chartType = document.querySelector('#chartType').value;
    const param = {};
    const noDataErrMsg = document.querySelector('#noDataErrMsg');
    noDataErrMsg.style.display="none";
    
    if(chartType === 'WEEKDAY') {
        param['mapNo'] = parseInt(document.querySelector('#map').value, 10);
    } else {
        if(category3.value !== 'all') {
            param['category3No'] = parseInt(category3.value, 10);
        }

        if(daysType.value === 'day') {
            param['sDate'] = sDate.value;
            param['eDate'] = eDate.value;
        } else {
            const endDate = eMonth.value.split('-');
            const lastDate = new Date(endDate[0],endDate[1],0);
            param['sDate'] = `${sMonth.value}-01`;
            param['eDate'] = `${eMonth.value}-${lastDate.getDate()}`;
        }
    }
    
    $.ajax({
        url: '/adm/analytics/getAnalyticsList.json',
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(param),
        success: (res) => {
            console.log(res);
            const { list } = res;
            if(list.length === 0) {
                noDataErrMsg.style.display="block";
            }
            const searchResult = {};
            
            if(chartType === 'DAYS') {
                if(category3.value === 'all') {
                    searchResult['all'] = list;
                } else {
                    list.forEach((data) => {
                        const { mapNo } = data;
                        if(searchResult[mapNo]) {
                            searchResult[mapNo].push(data);
                        } else {
                            searchResult[mapNo] = [data];
                        }
                    });
                }

                if(daysType.value === 'day') {
                    setChartByDay(searchResult);
                } else {
                    setChartByMonth(searchResult);
                }
            } else {

                setChartByWeekday(list);
            }
        }
    });
}

const setChartByMonth = (searchData) => {
    const category3Nm = category3.selectedOptions[0].innerText;
    const chartData = [];

    for(const k of Object.keys(searchData)) {
        const counting = {};
        searchData[k].forEach(data=> {
            const { regDt } = data;
            const date = regDt.split(' ')[0].split('-');
            const month = `${date[0]}-${date[1]}`;
            if(counting[month]) {
                counting[month] += 1;
            } else {
            counting[month] = 1;
            }   
        });
        chartData.push({
            x: Object.keys(counting),
            y: Object.values(counting),
            name: k === 'all'?'시스템총합': searchData[k][0].mapNm,
            hovertemplate : '<i>%{x}</i><br>' + '<b>이용횟수 : %{y}</b>',
        })
    }
    const layout = {
        showlegend: true,
        title: { 
            text: `${category3Nm} 월별 이용량 분석`,
            font: { 
                size: 20 
            },
        }
    };
    Plotly.newPlot(chartArea, chartData, layout, {locale: 'ko'});
}


const setChartByDay = (searchData) => {
    const category3Nm = document.querySelector('#category3').selectedOptions[0].innerText;
    const chartData = [];
    for(const k of Object.keys(searchData)) {
        const counting = {};
        searchData[k].forEach(data=> {
            const { regDt } = data;
            const date = regDt.split(' ')[0];
            if(counting[date]) {
                counting[date] += 1;
            } else {
            counting[date] = 1;
            }   
        });
        chartData.push({
            x: Object.keys(counting),
            y: Object.values(counting),
            name: k === 'all'?'시스템총합': searchData[k][0].mapNm,
            hovertemplate : '<i>%{x}</i><br>' + '<b>이용횟수 : %{y}</b>',
        })
    }
    const layout = {
        showlegend: true,
        title: { 
            text: `${category3Nm} 날짜별 이용량 분석`,
            font: { 
                size: 20 
            },
        }
    };
    Plotly.newPlot(chartArea, chartData, layout);
}

const setChartByWeekday = (searchData) => {
    const mapNm = document.querySelector('#map').selectedOptions[0].innerText;

    const chartData = new Array(24).fill(null).map(()=> new Array(7).fill(null));

    searchData.forEach((data) => {
        const { regDt } = data;
        const date = new Date(regDt);
        chartData[date.getHours()][date.getDay()] += 1;
    });

    const data = [{
        x: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        y: ['12AM', '1AM', '2AM', '3AM', '4AM', '5AM', '6AM', '7AM', '8AM', '9AM', '10AM', '11AM', '12PM', '1PM', '2PM', '3PM', '4PM', '5PM', '6PM', '7PM', '8PM', '9PM', '10PM', '11PM'],
        z: chartData,
        name: '',
        colorscale : 'YlGnBu',
        hovertemplate : '<i>%{x} %{y}</i><br>'+'<b>이용횟수 : %{z}</b>',
        type: 'heatmap',
    }];

    const layout = {
        title: { 
            text: `${mapNm} 요일별 시간대별 이용량 분석`, 
            font: {
                size: 20
            }
        }
    };

    Plotly.newPlot(chartArea, data, layout);

}

const downloadData = () => {
    const chartArea = document.querySelector('#chartArea');
    const data = chartArea.data;
    const layout = chartArea.layout;
    let strExport = '';

    if(document.querySelector('#chartType').value === 'DAYS') {
        strExport = '도면명,일자,이용횟수\n';
        
        data.forEach((map) => {
            for(let i=0;i<map.x.length;i+=1) {
                strExport += `${map.name},${map.x[i]},${map.y[i]}\n`;
            }
        })
    } else {
        const mapNm = map.selectedOptions[0].innerText;
        strExport = '도면명,요일,시간,이용횟수\n';
        
        for(let i=0;i<7;i+=1){
            let weekday = '';

            switch(data[0].x[i]) {
                case 'Sunday': weekday = '일요일'; break;
                case 'Monday': weekday = '월요일'; break;
                case 'Tuesday': weekday = '화요일'; break;
                case 'Wednesday': weekday = '수요일'; break;
                case 'Thursday': weekday = '목요일'; break;
                case 'Friday': weekday = '금요일'; break;
                case 'Saturday': weekday = '토요일'; break;
            }

            for(let j=0;j<24;j+=1) {
                const counting = data[0].z[j][i];
                strExport += `${mapNm},${weekday},${data[0].y[j]},${counting === null ? 0 : counting}\n`;
            }
        }
    }

    let link = document.createElement('a');
    link.style.display = 'none';
    document.body.appendChild(link);

    link.href = URL.createObjectURL(new Blob(["\ufeff" + strExport], { type: 'text/csv;charset=utf-8;' }));
    link.download = `${layout.title.text}.csv`;
    link.click();

    document.body.removeChild(link);
    link = null;
}


initDaySelect();
initMonthSelect();
getAnalyticsData();

