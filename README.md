# Комментарии к выполненному заданию

 1. [Общая информация](#Общая-информация)
 2. [Задание 1](#Задание-1)
 3. [Генератор заказов](#Генератор-заказов)
 4. [Ссылки на используемые источники](#Ссылки-на-используемые-источники)

## Общая информация

Программа написана с использованием *Java 8*.
В качестве IDE использовался *Eclipse*.

Для создания графического интерфейса использовался *JavaFX*.

Проект можно собрать при помощи комманды **mvn package**.

В папке **target** лежит собранный проект. Запустить его можно при помощи *testtask-0.1-TEST-jar-with-dependencies.jar*.

## Задание 1

Солвером первой задачи является класс **FirstTask**.

В нём присутствует несколько основных методов:
1. **generateRoutes** - из предоставленного списка заказов формируются маршруты, количество которых должно быть равно количеству машин в центре распределения.
Метод последовательно берёт заказы из списка и помещает его в случайный маршрут. После распределения всех заказов проходит проверка на наличие пустых маршрутов,
если таковые имеются, то распределение пройдёт заново, и так до тех пор, пока в каждом маршруте будет не менее одного заказа. После этого в начало и конец получившегося
списка добавляются заказы с идентификационным номером 0. Данный номер означает, что заказ соответствует координатам распределительного центра. Такие заказы в маршруте 
обозначают начало маршрута или возвращение машины в центр распределения после выполнения всех заказов.
2. Внутри предыдущего метода, в конце, вызывается метод **massCorrectingRouteList**. Его задача оценить насколько построенный маршрут удовлетворяет условиям вместимости 
машины. Из выбранного маршрута последовательно берутся заказы и масса каждого прибавляется в общую массу загруженных заказов. Если при добавлении очередного заказа, суммарная 
масса всех загруженных заказов превышает максимальную грузоподъёмность машины, то между этим и предыдущим заказом добавляется заказ с номером 0, то есть в маршрут будет добавлена
точка возвращения на базу, чтобы загрузить последующий заказы, если таковые имеются.

Например:
Создан маршрут из заказов №2 - 50 кг, №25 - 140 кг, №3 - 40, кг. Максимальная грузоподъёмность машины 200 кг. Изначально маршрут выглядит так: 0 -> 2 -> 25 -> 3 -> 0. 
Если попыться развезти эти заказы за один подход, то суммарная масса груза составит 230 кг, что превышает допустимое значение. Значит алгоритм изменит маршрут так, чтобы 
заказы 2 и 25 были доставлены за первый подход, а заказ 3 за второй. Таким образом итоговый маршрут будет выглядеть как 0 -> 2 -> 25 -> 0 -> 3 -> 0.

3. **generateSchedule** - для выбранного маршрута составляет расписание. Принцип работы метода: полученный маршрут разбивается на участки, состящие из одного заказа 
обозначающего распределительный центр и как минимум одного обычного заказа.

Например: Маршрут 0 - 2 - 6 - 10 - 0 - 3 - 0 - 25 - 31 - 0 Будет разбит на:
- 0 - 2 - 6 - 10
- 0 - 3
- 0 - 25 - 31

   Затем каждый подмаршрут обрабатывается и составляется список работ, которые необходимо выполнить для его реализации. Сущетвует 3 типа работ: *Упаковка* - класс **Packing**, 
   *Доставка* - класс **Delivery**, *Возвращение на базу* - класс **Return**. Реализация каждого класса описана в комментариях к коду.
   
   После обработки всех подмаршрутов, списки работ формируют один общий список работ, которые необходимо осуществить для реализации выбранного маршрута.

Таким образом, после выполнении всех методов, объект класса **FirstTask** будет хранить в себе список всех маршрутов и список работ, соответствующих каждому маршруту.

В окне программы будет выводиться информация о заказах, маршрутах и работах. В поле с результатми будет представлена длительность работы как каждого ресурса, так и каждой отдельной работы. 
Также будет выводиться информация о простоях и задержках.

Простои в данном случае - это время между двумя обычными заказами, в которое машина бездействует. 
Они появляются, если машина загружается сразу несколькими заказами, один из них доставлен, но для выполнения следующего заказа нужно выехать позже, чем закрылся предыдущий.

Задержка показывает на какое время машина опоздала при выполнении заказа.

## Генератор заказов

Для ускорения тестирования и создания некоторой непредсказуемости начальных данных, я решил генерировать заказы случайным образом, используя ряд ограничений. За данную функцию 
отвечает класс **OrderGenerator**.
1. Координаты заказа не должны выходить за пределы зоны обслуживания. В качестве границы я взял окружность с определённым радиусом. Этот радиус находится из условия, что 
время, за которое машина доедет от центра распределения до границы и обратно, не должно превышать длительности работы центра распределения. Проверку можно производить
по центральному углу, так как расстояние между двумя точками на поверхности Земли связано с ним линейной зависимостью.
2. Масса заказа генерируется случайно в диапазоне от 2 кг до максимальной грузоподъёмности машины.
3. Начальное время окна доступности определяется в диапазоне между "начало работы DC + 1 час : конец работы DC - 1 час". Конечное время окна доступности создаётся в диапазоне 
"начало окна доступности + 1 час : конец работы DC".
4. Время на упаковку и распаковку заказа генерируется в пределах от 5 и до 59 минут.

## Ссылки на используемые источники

Для определения расстояния между двумя точками на поверхности Земли, я использовал метрику [Great-circle distance](https://en.wikipedia.org/wiki/Great-circle_distance).