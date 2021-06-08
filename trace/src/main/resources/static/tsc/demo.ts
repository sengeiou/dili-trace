export class DemoClass {

    constructor(props) {
        // @ts-ignore
        $.makeArray("");
        _.chain([]).value();
        console.info("bcd")
    }
}

export namespace abc {
    export namespace def {
        export class A {
            constructor() {
            }

            hello: () => {};
        };

        export class B {
        };
    }
}
export namespace demo {
    export class Test1 {
        constructor() {
            let a: abc.def.A = new abc.def.A();
            (async () => {
                await a.hello();
            })();
        }
    }

    export class Test2 {
        constructor() {
        }
    }

}

interface A {
    name?: string;
    age: number;
}

let a = <A>{name: "abc"};
a.age = 23;
a.name = "ddd";
console.info(a);

interface B {
    (a: number, b: number): boolean;
}

let b: B = (a1, b1) => {
    return a1 > b1;
}

interface C {
    [index: number]: string;
}

let c: C = ['a', 'b'];

let d: Array<string> = ['a', 'b'];
let e: [number] = [1];

let f: number[] = [1, 2, 3];
let g: [number, string] = [1, 'ss'];

interface E {
    name: string;
    age: number;
}

class ImplE implements E {
    readonly age: number;
    name: string;

    constructor() {
        this.age = 23;
        this.name = "abc";
    }
}

var e2 = new ImplE();
e2.name = "ss";

type sss = { name: string, age: number };
let ss: sss = {name: '', age: 34};

let n: { name: string, age: number } = {age: 23, name: ''};


// e2.age=44; error

console.info("abcd")
interface IAge{
    age:number;
}
interface IInfo{
    id:number;
    name:string;
}


function testFun<T extends IInfo&IAge>(data:T){
    console.info(data.id);
    console.info(data.age);
    console.info(data.name);
}
type Add = (x: number, y?: number) => number ;//类似接口
let funcAdd:Add=(a:number,b:number)=>{return a+b;} ;//类似实现类
let ret=funcAdd(1,3)
console.info(ret);

let u:IInfo={} as IInfo;
//@ts-ignore
let u2:User={};


