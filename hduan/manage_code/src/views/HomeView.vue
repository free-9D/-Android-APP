<template>
	<div class="home_view">
		<div class="projectTitle">欢迎使用 {{projectName}}</div>
		<div class="card_list">
		</div>
	</div>
</template>

<script setup>
	import {
		inject,
		nextTick,
		ref,
		getCurrentInstance
	} from 'vue';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const projectName = context.$project.projectName
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	const init=()=>{
	}
	init()
</script>
<style lang="scss" scoped>
	// 首页盒子
	.home_view {
	}
	.home_view .projectTitle{
	}

	.showIcons {
		transition: transform 0.3s;
		margin-right: 10px;
	}

	.showIcons1 {
		transform: rotate(-180deg);
	}
	
	// 总数盒子
	.count_list{
		// 总数card
		.card_view {
			// card头部
			.el-card__header {
				// 头部盒子
				.index_card_head {
					// 标题
					.card_head_title {
					}
					// 按钮盒子
					.card_head_right {
						// 按钮
						.el-icon {
						}
					}
				}
			}
			// body
			.el-card__body {
				// body盒子
				.count_item{
					// 总数标题
					.count_title{
					}
					// 总数数字
					.count_num{
					}
				}
			}
		}
	}

	// 统计图盒子
	.card_list {
		// 统计图card
		.card_view {
			// 头部
			.el-card__header {
				// 头部盒子
				.index_card_head {
					// 标题
					.card_head_title {
					}
					// 按钮盒子
					.card_head_right {
						// 按钮
						.el-icon{
						}
					}
				}
			}
			// body
			.el-card__body {
				// body盒子
				.card_item{
				}
			}
		}
	}
</style>
<style>
.home_view .projectTitle {
    display: none;
}
.home_view .el-card__header {display: none;}

.home_view {
    width: 100%;
    padding:20px;
    display: flex;
    flex-wrap: wrap;
    margin-bottom:30px;
}

.home_view .count_list {
    width: calc(100% - 20px);
    display: flex;
    flex-wrap: wrap;
    justify-content:center;
    margin:0 10px 30px;
    row-gap: 30px;
    column-gap: 30px;
    order: 1;
}
.home_view .count_list .card_view {
    flex: 1;
    height: 100px;
    box-shadow: none;
    background-image: url(http://clfile.zggen.cn/20240922/19482a498b9a4360a873a296ac700338.webp);
    background-size: 100% 100%;
    border-radius: 60px 0 0 60px;
    min-width: 200px;
    max-width: 20%;
}

.home_view .count_title {
    font-weight: 400;
    font-size: 14px;
    order: 1;
    color: #969696;
}

.home_view .count_num {font-size: 30px;color: #000000;}

.home_view .count_item {
    display: flex;
    flex-direction: column;
    text-align: center;
}

.home_view .card_list {
    width: calc(100% + 20px);
    margin:0 0 0 0px;
    padding:0;
    position: relative;
    display:flex;
    flex-wrap:wrap;
    justify-content:center;
    order: 3;
}
.home_view .card_list .card_item .Echart {
    height: 342px!important;
}
.home_view .card_list .card_view {
    width:calc(50% - 20px);
    margin:0 10px 20px;
    background: none;
    box-shadow: none;
    padding: 15px;
    border: 5px solid;
    border-image: linear-gradient(135deg, rgba(255, 255, 255, 1), rgba(137.00000703334808, 196.00000351667404, 133.00000727176666, 1)) 5 5;
}

.home_view .card_list .card_view .el-card__body {
    background: #fff;
}

.home-calendar{
  width: calc(100% - 20px);
  box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 0px;
  margin: 0px 10px 30px;
  color: rgb(102, 102, 102);
  font-size: inherit;
  height: auto;
  border: 5px solid;
  border-image: linear-gradient(135deg, rgba(255, 255, 255, 1), rgba(137.00000703334808, 196.00000351667404, 133.00000727176666, 1)) 5 5;
  padding: 14px;
  order: 2;
  min-width: 414px;
}
.home-calendar .header{
  padding: 14px 20px;
  flex-wrap: wrap;
  display: flex;
  width: 100%;
  justify-content: space-between;
  height: auto;
  color: var(--theme);
  font-size: 14px;
  background: #fff;
}
.home-calendar .header .btn{
  cursor: pointer;
  border: 0px solid rgb(153, 153, 153);
  align-items: center;
  border-radius: 4px;
  display: flex;
  width: auto;
  justify-content: center;
}
.home-calendar .header .title{
  align-items: center;
  justify-content: center;
  display: flex;
}
.home-calendar table{
  width: 100%;
  height: auto;
  background: #fff;
}
.home-calendar tbody{
  width: 100%;
  border-bottom: 10px solid #fff;
  border-right: 10px solid #fff;
}
.home-calendar tr{
  width: 100%;
  align-items: center;
  justify-content: center;
  display: flex;
}
.home-calendar th{
  align-items: center;
  flex: 1;
  display: flex;
  line-height: 30px;
  justify-content: center;
}
.home-calendar td{
  cursor: pointer;
  padding: 4px;
  flex: 1;
  display: flex;
  justify-content: center;
  text-align: center;
  font-size: 14px;
  color: #000;
  line-height: 32px;
}

.old {display: none;}

.festival.today {
    color: #fff;
    background: var(--theme);
}
</style>
