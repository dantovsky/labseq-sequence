import { Component } from '@angular/core';
import { LabseqService } from './labseq.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-labseq',
  templateUrl: './labseq.component.html',
  styleUrls: ['./labseq.component.scss'],
  standalone: true, // Disponível a partir do Angular 14
  imports: [FormsModule, CommonModule]
})
export class LabseqComponent {
  n: number | null = null;
  result: string = '';
  loading: boolean = false;

  constructor(private labseqService: LabseqService) {}

  calculate() {
    if (this.n === null || this.n < 0) {
      this.result = 'Please enter a valid non-negative number.';
      return;
    }
    this.loading = true;
    this.labseqService.calculate(this.n)
      .subscribe((res: string) => {
        this.result = res;
        this.loading = false;
      }, (err: any) => {
        this.result = 'Error: ' + err.message
        this.loading = false;
      }
    );
  }

  clearCache() {
    this.labseqService.clearCache()
      .subscribe((res: string) => this.result = res, (err: any) => this.result = 'Error: ' + err.message);
  }

  printCache() {
    this.labseqService.printCache()
      .subscribe((res: string) => this.result = res, (err: any) => {
        // Se o backend retorna texto puro, a mensagem estará em err.error
        if (err.status === 400 && err.error) {
          this.result = err.error;
        } else {
          this.result = 'Error: ' + (err.message || JSON.stringify(err));
        }
      });
  }

  getCacheSize() {
    this.labseqService.getCacheSize()
      .subscribe((res: string) => this.result = res, (err: any) => this.result = 'Error: ' + err.message);
  }
}