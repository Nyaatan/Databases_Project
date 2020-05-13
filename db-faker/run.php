<?php 
require './vendor/autoload.php';

$faker = \Faker\Factory::create('pl_PL');
\Bezhanov\Faker\ProviderCollectionHelper::addAllProvidersTo($faker);

// Ustawienia
$emp_count = 20;                        // Liczba pracowników
$shop_count = 15;                       // Liczba sklepów
$order_count = 30;                      // Liczba zamówień
$prod_count = 100;                       // Liczba rodzajów produktów

$salary_range = [2, 6];                 // Zakres pensji pracowników (w tysiącach)
$order_status = [                       // Możliwe statusy zamówień
    'In progress',
    'Waiting',
    'Completed',
    'Cancelled'
];
$prod_weight_range = [5, 12];           // Zakres wagi produktu (w setkach gram)
$prod_quantity_range = [200, 1500];     // Zakres ilości produktu w magazynie
$prod_in_order_range = [2, 5];          // Zakres ilości różnych produktów w zamówieniu
$prod_in_order_quantity_range = [2, 20]; // Zakres ilości produktu w zamówieniu (w dziesiątkach)

function display($string){
    echo $string . PHP_EOL;
}

// pracownicy
for($i = 0; $i < $emp_count; $i++){
    $morf = $faker->randomElement(['male', 'female']);
    display('INSERT INTO "WH_Employees" ("Employee_id", "First_name", "Last_name", "Salary")');
    display("VALUES (". ($i+1) .", '". $faker->firstName($morf) ."', '". $faker->lastName($morf) ."', ". $faker->numberBetween($salary_range[0], $salary_range[1]) * 1000 .");");
}
display('COMMIT;' . PHP_EOL);

// sklepy
for ($i = 0; $i < $shop_count; $i++){
    display('INSERT INTO "WH_Shops"');
    display("VALUES (". ($i+1) .", '". $faker->address ."', '". $faker->company ."');");
}
display('COMMIT;' . PHP_EOL);

// zamówienia
for ($i = 0; $i < $order_count; $i++){
    display('INSERT INTO "WH_Orders"');
    display("VALUES (". ($i+1) .", ". $faker->numberBetween(1, $shop_count) .", ". $faker->numberBetween(1, $emp_count) .", '". $faker->randomElement($order_status) ."');");
}
display('COMMIT;' . PHP_EOL);

// Produkty
for ($i = 0; $i < $prod_count; $i++){
    display('INSERT INTO "WH_Products"');
    display("VALUES (". ($i+1) .", '". $faker->ingredient ." ". $faker->numberBetween($prod_weight_range[0], $prod_weight_range[1])*100 ."g', ". $faker->numberBetween($prod_quantity_range[0], $prod_quantity_range[1]) .");");
}
display('COMMIT;' . PHP_EOL);

// Produkty w zamówieniach
$key = 1;
for ($i = 0; $i < $order_count; $i++){
    $prod_in_order = $faker->numberBetween($prod_in_order_range[0], $prod_in_order_range[1]);
    $used_ids = [];
    for($j = 0; $j < $prod_in_order; $j++){
        do{
            $product_id = $faker->numberBetween(1, $prod_count);
        }while(in_array($product_id, $used_ids));
        
        display('INSERT INTO "WH_Ordered_products"');
        display("VALUES (". $key .", ". ($i+1) .", ". $product_id .", ". $faker->numberBetween($prod_in_order_quantity_range[0], $prod_in_order_quantity_range[1])*10 .");");
        $used_ids[] = $product_id;
        $key++;
    }
}
display('COMMIT;' . PHP_EOL);
?>